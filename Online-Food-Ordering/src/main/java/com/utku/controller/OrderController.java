package com.utku.controller;


import com.utku.model.CartItem;
import com.utku.model.Order;
import com.utku.model.OrderItem;
import com.utku.model.User;
import com.utku.request.AddCartItemRequest;
import com.utku.request.OrderRequest;
import com.utku.service.OrderService;
import com.utku.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;




    @PostMapping("/cart/add")
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderRequest req,
            @RequestHeader("Authorization")String jwt
    )throws Exception{

        User user = userService.findUserByJwtToken(jwt);

        Order order = orderService.createOrder(req,user );
        return new ResponseEntity<>(order, HttpStatus.OK);
    }


    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getOrderHistory(
            @RequestBody OrderRequest req,
            @RequestHeader("Authorization")String jwt
    )throws Exception{

        User user = userService.findUserByJwtToken(jwt);

       List <Order> orders = orderService.getUsersOrder(user.getId() );
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
