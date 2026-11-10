package com.mt03062.foodapp.menu;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MenuController {

    private final RestaurantRepository restaurants;
    private final MenuItemRepository items;

    public MenuController(RestaurantRepository restaurants, MenuItemRepository items) {
        this.restaurants = restaurants;
        this.items = items;
    }

    // COMMAND
    @PostMapping("/restaurants")
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurant createRestaurant(@RequestBody Restaurant r) {
        if (r.name == null || r.name.isBlank() || r.city == null || r.city.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name and city are required");
        }
        r.id = null;
        return restaurants.save(r);
    }

    // QUERY
    @GetMapping("/restaurants")
    public List<Restaurant> listRestaurants(@RequestParam(required = false) String city) {
        return city == null ? restaurants.findAll() : restaurants.findByCity(city);
    }

    // COMMAND
    @PostMapping("/restaurants/{rid}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItem addItem(@PathVariable Long rid, @RequestBody MenuItem item) {
        if (!restaurants.existsById(rid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant not found");
        }
        if (item.name == null || item.name.isBlank() || item.price <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name and a positive price are required");
        }
        item.id = null;
        item.restaurantId = rid;
        item.available = true;
        return items.save(item);
    }

    // QUERY
    @GetMapping("/restaurants/{rid}/menu")
    public List<MenuItem> getMenu(@PathVariable Long rid) {
        if (!restaurants.existsById(rid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "restaurant not found");
        }
        return items.findByRestaurantId(rid);
    }

    // QUERY (called by order-service)
    @GetMapping("/items/{id}")
    public MenuItem getItem(@PathVariable Long id) {
        return items.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "item not found"));
    }

    // COMMAND
    @PutMapping("/items/{id}/availability")
    public MenuItem setAvailability(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        MenuItem item = getItem(id);
        item.available = body.getOrDefault("available", true);
        return items.save(item);
    }
}
