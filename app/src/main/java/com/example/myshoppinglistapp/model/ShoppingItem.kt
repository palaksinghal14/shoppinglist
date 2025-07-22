package com.example.myshoppinglistapp.model

data class ShoppingItem( var id:Int,
                         var name :String ,
                         var quantity: Int?,
                         var isediting:Boolean=false
)