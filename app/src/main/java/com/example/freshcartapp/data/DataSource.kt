package com.example.freshcartapp.data

import com.example.freshcartapp.R


object DataSource{
    fun loadCategories() : List<Categories>{
        return listOf(
            Categories(stringResourceId = R.string.fruits, imageResourceId = R.drawable.fruits),
            Categories(R.string.vegetables, R.drawable.vegetables),
            Categories(R.string.beverages, R.drawable.bevarages),
            Categories(R.string.packaged, R.drawable.packaged),
            Categories(R.string.kitchen, R.drawable.kitchen),
            Categories(R.string.stationary, R.drawable.stationary),
            Categories(R.string.dairy, R.drawable.dairy),
            Categories(R.string.bath, R.drawable.bath),
            Categories(R.string.meat, R.drawable.meat),
            Categories(R.string.noodles, R.drawable.noodles),
        )


    }

    /**************** FRUITS *****************/
//
//    fun loadItems(@StringRes categoryName: Int): List<Item>{
//        return listOf(
//            Item(R.string.apple, R.drawable.apple, R.string.fruits, 100, "1Kg"  ),
//            Item(R.string.mango, R.drawable.mango, R.string.fruits, 250, "1Kg"  ),
//            Item(R.string.watermelon, R.drawable.watermelon, R.string.fruits, 100, "1Kg"  ),
//            Item(R.string.orange, R.drawable.orange, R.string.fruits, 200, "1Kg"  ),
//            Item(R.string.papaya, R.drawable.papaya, R.string.fruits, 800, "1Kg"  ),
//            Item(R.string.pear, R.drawable.pear, R.string.fruits, 210, "1Kg"  ),
//            Item(R.string.pineapple, R.drawable.pineapple, R.string.fruits, 300, "1Kg"  ),
//            Item(R.string.grapes, R.drawable.grapes, R.string.fruits, 120, "1Kg"  ),
//            Item(R.string.strawberry, R.drawable.strawberry, R.string.fruits, 280, "1Kg"  ),
//            Item(R.string.kiwi, R.drawable.kiwi, R.string.fruits, 120, "1Kg"  ),
//            Item(R.string.pepsi, R.drawable.pepsi, R.string.beverages, 120, "1Kg"  )
//
//        ).filter {
//            it.itemCategoryId == categoryName
//        }
//    }

}