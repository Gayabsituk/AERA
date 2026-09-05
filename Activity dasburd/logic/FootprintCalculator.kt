package com.example.activity.logic

import com.example.activity.data.ActivityCategory

object FootprintCalculator {
    fun calculate(
        category: ActivityCategory,
        type: String,
        value: Double, // distance, kwh, amount, items, or weight
        trips: Int = 1,
        passengers: Int = 1
    ): Double {
        return when (category) {
            ActivityCategory.TRANSPORT -> {
                val factor = getTransportFactor(type)
                (value * trips * factor) / passengers
            }
            ActivityCategory.ENERGY -> value * 0.70
            ActivityCategory.FOOD -> value * getFoodFactor(type)
            ActivityCategory.SHOPPING -> value * getShoppingFactor(type)
            ActivityCategory.WASTE -> value * getWasteFactor(type)
        }
    }

    private fun getTransportFactor(type: String): Double = when (type) {
        "Motorcycle" -> 0.266
        "Tricycle" -> 0.266
        "Car" -> 0.319
        "Taxi" -> 0.292
        "Jeepney" -> 0.415
        "Bus" -> 1.097
        "UV/Van" -> 0.415
        else -> 0.0
    }

    private fun getFoodFactor(type: String): Double = when (type) {
        "High-Impact Meat (Beef)" -> 27.0
        "Medium-Impact Meat (Pork)" -> 8.0
        "Low-Impact Meat (Chicken)" -> 6.0
        "Seafood" -> 5.0
        "Dairy & Eggs" -> 4.0
        "Grains & Staples" -> 2.5
        "Fruits & Vegetables" -> 0.9
        "Legumes & Plant-Based" -> 1.0
        "Processed & Packaged Foods" -> 2.5
        "Sweets & Desserts" -> 5.0
        "Beverages" -> 1.5
        else -> 0.0
    }

    private fun getShoppingFactor(type: String): Double = when (type) {
        "Clothing & Footwear" -> 15.0
        "Electronics" -> 100.0
        "Furniture" -> 50.0
        "Household Products" -> 5.0
        "Personal Care" -> 2.0
        "Paper Products" -> 1.5
        "Plastic Products" -> 3.0
        "Other Retail Goods" -> 5.0
        else -> 0.0
    }

    private fun getWasteFactor(type: String): Double = when (type) {
        "Food Waste" -> 0.5
        "Paper & Cardboard" -> 1.5
        "Plastic" -> 3.0
        "Glass" -> 0.9
        "Metal" -> 2.0
        "Textiles" -> 5.0
        "Electronic Waste" -> 20.0
        "Mixed/General Waste" -> 2.0
        else -> 0.0
    }
}
