package com.murad.androiddevtask.data

import com.murad.androiddevtask.R
import com.murad.androiddevtask.model.Category
import com.murad.androiddevtask.model.Statement
import kotlin.random.Random


object FakeData {

    val categories = arrayOf(
        R.drawable.ic_airplane to "Airlines",
        R.drawable.ic_rent to "Rent A Car",
        R.drawable.ic_bed to "Hotels And Motels",
        R.drawable.ic_transport to "Transport",
        R.drawable.ic_car to "Cars And Vehicles",
        R.drawable.ic_government to "Government Services",
        R.drawable.ic_person to "Personal Services",
        R.drawable.ic_professional to "Professional Services",
        R.drawable.ic_business to "Business Services",
        R.drawable.ic_repair to "Repair Services",
        R.drawable.ic_clothing to "Clothing Services",
        R.drawable.ic_sale_by_mail to "Sale By Mail Or Telephone",
        R.drawable.ic_popcorn to "Entertainment",
        R.drawable.ic_manufacturer to "Wholesale Suppliers And Manufacturers",
        R.drawable.ic_membership to "Membership Organizations",
        R.drawable.ic_cash to "Cashout",
        R.drawable.ic_money_transfer to "Money Transfers",
        R.drawable.ic_other to "Others",
        R.drawable.ic_contract_service to "Contract Services",
        R.drawable.ic_gear to "Other Services",
        R.drawable.ic_retail to "Retail",
        R.drawable.ic_stores to "Other Stores",
        R.drawable.ic_health to "Health"
    ).mapIndexed { i, pair ->
        Category(i.toLong(), pair.first, pair.second, Random.nextInt(8, 22), 1500)
    }

    val statements = listOf(
        Statement(R.drawable.ic_airplane, "Uber trip 20 UAW", "13:02 17.09.2018", 100),
        Statement(R.drawable.ic_airplane, "Uber trip 20 UAW", "13:02 17.09.2018", 100),
        Statement(R.drawable.ic_airplane, "Uber trip 20 UAW", "13:02 17.09.2018", 100),
        Statement(R.drawable.ic_uber, "Uber trip 20 UAW", "13:02 17.09.2018", 100),
        Statement(R.drawable.ic_uber, "Uber trip 20 UAW", "13:02 17.09.2018", 100),
        Statement(R.drawable.ic_airplane, "Uber trip 20 UAW", "13:02 17.09.2018", 100),
        Statement(R.drawable.ic_airplane, "Uber trip 20 UAW", "13:02 17.09.2018", 100),
    )

    // for Stats fragment
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val cards = listOf(
        "Espresso MC **4554",
        "Espresso MC **4555",
        "Espresso MC **4556",
        "Espresso MC **4557",
    )

}
