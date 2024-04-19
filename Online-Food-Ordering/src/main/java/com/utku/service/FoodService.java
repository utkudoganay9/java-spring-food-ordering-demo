package com.utku.service;

import com.utku.model.Category;
import com.utku.model.Food;
import com.utku.model.Restaurant;
import com.utku.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

    public Food createFood(CreateFoodRequest req,
                           Category category, Restaurant restaurant);

    void deleteFood(Long foodId) throws Exception;

    public List<Food> getRestaurantFood(Long restaurantId,
                                        boolean isVegetarian,
                                        boolean isNonVegetarian,
                                        
                                        String foodCategory);

    public List<Food>searchFood(String keyword);
    public Food findFoodById(Long foodId) throws Exception;

    public Food updateAvailabilityStatus(Long foodId) throws Exception;
}
