import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    Restaurant restaurant;

    @BeforeEach
    public void setup() {
        restaurant = new Restaurant("Amelie's Cafe","Chennai",LocalTime.parse("10:30:00"),LocalTime.parse("22:00:00"));
        restaurant.addToMenu("Sweet Corn Soup",119);
        restaurant.addToMenu("Vegetable Lasagne",269);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time() {
        restaurant.openingTime = LocalTime.now().minusHours(5);
        restaurant.closingTime = LocalTime.now().plusHours(1);
        assertTrue(restaurant.isRestaurantOpen());
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time() {
        restaurant.openingTime = LocalTime.now().minusHours(5);
        restaurant.closingTime = LocalTime.now().minusHours(1);
        assertFalse(restaurant.isRestaurantOpen());
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1(){
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws ItemNotFoundException {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable Lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(ItemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //>>>>>>>>>>>>>>>>>>>>>>ORDER COST<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Test
    public void get_correct_order_cost_when_one_or_more_items_are_selected() throws ItemNotFoundException {
        List<Item> selectedItems = restaurant.getMenu();
        int expectedOrderCost = 388;

        MatcherAssert.assertThat(restaurant.getOrderCost(selectedItems),Matchers.equalTo(expectedOrderCost));

        restaurant.addToMenu("Sizzling Brownie",319);
        expectedOrderCost += 319;

        MatcherAssert.assertThat(restaurant.getOrderCost(selectedItems),Matchers.equalTo(expectedOrderCost));
    }

    @Test
    public void get_order_cost_as_zero_by_default_when_no_items_are_selected() throws ItemNotFoundException {
        MatcherAssert.assertThat(restaurant.getOrderCost(new ArrayList<Item>()),Matchers.equalTo(0));
    }

    @Test
    public void get_updated_order_cost_when_removing_the_selected_items() throws ItemNotFoundException {
        List<Item> selectedItems = restaurant.getMenu();
        int expectedOrderCost = 388;

        MatcherAssert.assertThat(restaurant.getOrderCost(selectedItems), Matchers.equalTo(expectedOrderCost));

        selectedItems.remove(1);
        expectedOrderCost -= 269;

        MatcherAssert.assertThat(restaurant.getOrderCost(selectedItems), Matchers.equalTo(expectedOrderCost));
    }

    @Test
    public void edge_case_selected_item_got_removed_by_admin_from_menu_at_the_same_instant_should_throw_exception() throws ItemNotFoundException {
        List<Item> selectedItems = new ArrayList<>();
        selectedItems.addAll(restaurant.getMenu());
        restaurant.removeFromMenu("Vegetable Lasagne");
        assertThrows(ItemNotFoundException.class,()->restaurant.getOrderCost(selectedItems));
    }

    //<<<<<<<<<<<<<<<<<<<<ORDER COST>>>>>>>>>>>>>>>>>>>>>>>>>>
}