package com.example.productfinding.util;

import android.location.Location;

import com.example.productfinding.model.Catalog;
import com.example.productfinding.model.Shop;

import java.util.Collections;
import java.util.List;

public class ResultListUtil {
    public static final boolean ACCENDING = true;
    public static final boolean DECENDING = false;

    public static void sortShopListResultBy(List<Shop> itemList, Location mLocation, boolean sortingCondition) {
        //Sort the Distance by Smaller to Larger
        Collections.sort(itemList, (o1, o2) -> {

            // -1 :less than, 1 :greater than, 0 :equal, all inversed for descending
            Location o1ShopLocation = new Location("");
            o1ShopLocation.setLatitude(o1.getLatLng().latitude);
            o1ShopLocation.setLongitude(o1.getLatLng().longitude);

            Location o2ShopLocation = new Location("");
            o2ShopLocation.setLatitude(o2.getLatLng().latitude);
            o2ShopLocation.setLongitude(o2.getLatLng().longitude);

            float o1Distance = mLocation.distanceTo(o1ShopLocation);
            float o2Distance = mLocation.distanceTo(o2ShopLocation);

            //Accending Sort == True
            if (sortingCondition)
                return o1Distance == o2Distance ? 0 : o1Distance > o2Distance ? 1 : -1;
            else
                return o1Distance == o2Distance ? 0 : o1Distance > o2Distance ? -1 : 1;
        });
    }

    /**
     * Sort the Distance by Smaller to Larger if sortingCondition is True<br>
     * Sort the Distance by Larger to Smaller if sortingCondition is False<br>
     *
     * @param catalogList
     * @param mLocation
     * @param sortingCondition
     */
    public static void sortCatalogListBy(List<Catalog> catalogList, Location mLocation, boolean sortingCondition) {
        Collections.sort(catalogList, (o1, o2) -> {
            Location o1ShopLocation = new Location("");
            o1ShopLocation.setLatitude(o1.getShop().getLatitude());
            o1ShopLocation.setLongitude(o1.getShop().getLongitude());

            Location o2ShopLocation = new Location("");
            o2ShopLocation.setLatitude(o2.getShop().getLatitude());
            o2ShopLocation.setLongitude(o2.getShop().getLongitude());

            float o1Distance = mLocation.distanceTo(o1ShopLocation);
            float o2Distance = mLocation.distanceTo(o2ShopLocation);

            /*Accending Sort ==True*/
            if (sortingCondition)
                return o1Distance == o2Distance ? 0 : o1Distance > o2Distance ? 1 : -1;
            else
                return o1Distance == o2Distance ? 0 : o1Distance > o2Distance ? -1 : 1;
        });
    }
}
