package com.utku.service;

import com.utku.model.Category;
import com.utku.model.Food;
import com.utku.model.Restaurant;
import com.utku.repository.FoodRepository;
import com.utku.request.CreateFoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImp implements FoodService {

    @Autowired
    private FoodRepository foodRepository;


    @Override
    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) {

        Food food = new Food();
        food.setFoodCategory(category);
        food.setRestaurant(restaurant);
        food.setImages(req.getImages());
        food.setDescription(req.getDescription());
        food.setName(req.getName());
        food.setPrice(req.getPrice());
        food.setIngredients(req.getIngredientsItems());
        food.setVegetarian(req.isVegetarian());

        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);
        return savedFood;
    }

    @Override
    public void deleteFood(Long foodId) throws Exception {

        Food food = findFoodById(foodId);
        food.setRestaurant(null);
        foodRepository.save(food);


    }

    @Override
    public List<Food> getRestaurantFood(Long restaurantId, boolean isVegetarian,
                                        boolean isNonVegetarian,
                                        boolean isSeasonal,
                                        String foodCategory) {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);

        if (isVegetarian) {
            foods = filterByVegetarian(foods, isVegetarian);
        }
        if (isNonVegetarian) {
            foods = filterByNonVegetarian(foods, isNonVegetarian);
        }
        if (isSeasonal) {
            foods = filterBySeasonal(foods, isSeasonal);
        }

        if (foodCategory!=null && !foodCategory.equals("")) {
            foods = filterByCategory(foods, foodCategory);
        }


        return foods;
    }




    private List<Food> filterByVegetarian(List<Food> foods, boolean isVegetarian) {
        return foods.stream().filter(food->food.isVegetarian()==isVegetarian).
                collect(Collectors.toList());
    }

    private List<Food> filterByNonVegetarian(List<Food> foods, boolean isNonVegetarian) {
        return foods.stream().filter(food->food.isVegetarian()==false).
                collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasonal) {
        return foods.stream().filter(food->food.isSeasonal()==isSeasonal).
                collect(Collectors.toList());
    }
    private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
        return foods.stream().filter(food -> {
           if (food.getFoodCategory()!=null){
               return food.getFoodCategory().getName().equals(foodCategory);
           }
           return false;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) {


        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {

        Optional<Food> optionalFood = foodRepository.findById(foodId);

        if (optionalFood.isEmpty()){
            throw new Exception("Food is not exist");
        }

        return optionalFood.get();
    }

    @Override
    public Food updateAvailabilityStatus(Long foodId) throws Exception {

        Food food = findFoodById(foodId);
        food.setAvaliable(!food.isAvaliable());


        return foodRepository.save(food);
    }
}
