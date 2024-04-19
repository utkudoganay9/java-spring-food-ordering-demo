package com.utku.service;

import com.utku.model.Cart;
import com.utku.model.CartItem;
import com.utku.model.Food;
import com.utku.model.User;
import com.utku.repository.CartItemRepository;
import com.utku.repository.CartRepository;
import com.utku.repository.FoodRepository;
import com.utku.repository.UserRepository;
import com.utku.request.AddCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CartServiceImp implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;


    @Override
    public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        Food food = foodService.findFoodById(req.getFoodId());

        Cart cart = cartRepository.findByCustomerId(user.getId());

        for (CartItem cartItem : cart.getCartItem()) {
            if (cartItem.getFood().equals(food)) {
                int newQuantity = cartItem.getQuantity() + req.getQuantity();
            }
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setFood(food);
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setIngredients(req.getIngredients());
        newCartItem.setTotalPrice(req.getQuantity() * food.getPrice());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);
        cart.getCartItem().add(savedCartItem);

        return savedCartItem;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {

        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        if (optionalCartItem.isEmpty()) {
            throw new Exception("Cart item cannot found");
        }
        CartItem item = optionalCartItem.get();
        item.setQuantity(quantity);
        item.setTotalPrice(item.getFood().getPrice() * quantity);

        return cartItemRepository.save(item);
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);


        Cart cart = cartRepository.findByCustomerId(user.getId());

        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        if (optionalCartItem.isEmpty()) {
            throw new Exception("Cart item cannot found");
        }
        CartItem item = optionalCartItem.get();

        cart.getCartItem().remove(item);


        return cartRepository.save(cart);
    }

    @Override
    public Long calculateCartTotals(Cart cart) throws Exception {

        Long total = 0L;

        for (CartItem cartItem : cart.getCartItem()) {
            total += cartItem.getFood().getPrice() * cartItem.getQuantity();
        }


        return total;
    }

    @Override
    public Cart findCartById(Long id) throws Exception {

        Optional<Cart> optionalCart=cartRepository.findById(id);
        if (optionalCart.isEmpty()){
            throw new Exception("Cart cannot found with id"+id);

        }

        return optionalCart.get();
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {

       // User user = userService.findUserByJwtToken(userId)


        Cart cart = cartRepository.findByCustomerId(userId);
        cart.setTotal(calculateCartTotals(cart));
     return cart;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {

       // User user = userService.findUserByJwtToken(userId);


        Cart cart = findCartByUserId(userId);

        cart.getCartItem().clear();

        return cartRepository.save(cart);
    }
}
