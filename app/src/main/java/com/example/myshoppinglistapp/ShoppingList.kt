package com.example.myshoppinglistapp

import android.graphics.Paint.Align
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myshoppinglistapp.model.ShoppingItem

@Preview
@Composable
fun shoppinglistapp() {
    var itemname by remember { mutableStateOf("") }
    var itemquantity by remember { mutableStateOf("") }
    var showdialog by remember { mutableStateOf(true) }
    var list by remember { mutableStateOf(listOf<ShoppingItem>()) }


    // The first main screen

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { showdialog = true },
        ) {
            Text(text = "Add item")
        }

        LazyColumn() {
            items(list) { item ->
                if (item.isediting){
                   shoppingitemeditor(item = item,
                       oneditcomplete = {editedname, editedquantity->
                           list=list.map{it.copy(isediting=false)}
                        val editeditem= list.find{it.id==item.id}
                           editeditem?.let{
                               it.name=editedname
                               it.quantity=editedquantity
                           }

                       }
                      )

                }
                else{
                    shoppinglistitem(item = item,
                        onEditClick = {
                            list =list.map { it.copy(isediting = it.id == item.id) }
                    },
                        onDeleteClick = {
                        list=list-item
                    }
                    )
                }

            }
        }

    }

    //  after clicking on add item , a dialog box will appear

    if (showdialog) {
        AlertDialog(
            onDismissRequest = {showdialog = false },
            confirmButton = {  Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if(itemname.isNotBlank())
                    {
                        val newitem=ShoppingItem( id=list.size +1,
                                                   name=itemname,
                                                 quantity = itemquantity.toIntOrNull()?:1 // if found null or anything that cannot be converted into int , it would be 1
                        )
                        list=list+newitem // .add will not be use because that will not internally change the list
                        showdialog=false  // so that after adding the element , alertdialog gets off
                        itemname=""// declaring its empty again otherwise this if blocks run again and again
                    }

                }
                ) {
                    Text(text = "Add")
                }
                Button(onClick = { showdialog=false}) {
                    Text(text = "cancel")
                }
            }
         },
            title = {
                Text(
                    text = "Add Shopping Item ",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },

            text = {
                Column(modifier = Modifier.padding(8.dp)) {
                    OutlinedTextField(value = itemname,
                        onValueChange = { itemname = it })

                    Spacer(modifier = Modifier.padding(8.dp))

                    OutlinedTextField(value = itemquantity,
                        onValueChange = { itemquantity = it })

                }
            }
        )
    }
}

// item of the list

@Composable
fun shoppinglistitem(
    item: ShoppingItem,
    onEditClick:()->Unit,
    onDeleteClick:()->Unit
){
     Row(modifier= Modifier
         .padding(8.dp)
         .fillMaxWidth()
         .border(
             border = BorderStroke(2.dp, color = Color.Cyan),
             shape = RoundedCornerShape(20),
         ),
         horizontalArrangement = Arrangement.SpaceBetween
     ) {
         Text(text = "name: ${item.name} ")
         Text(text = "quantity: ${item.quantity}")
         Row (modifier=Modifier.padding(8.dp)){
         IconButton(onClick = onEditClick) {
             Icon(imageVector = Icons.Default.Edit, contentDescription = null)
         }
         IconButton(onClick = onDeleteClick) {
             Icon(imageVector = Icons.Default.Delete, contentDescription = null)
         }
     }
     }
}


// Editing function

@Composable
fun shoppingitemeditor(item:ShoppingItem,
                       oneditcomplete:(String,Int)-> Unit )
{
    var editedname by remember { mutableStateOf(item.name) }
    var editedquantity by remember { mutableStateOf(item.quantity.toString()) }
    var isediting by remember { mutableStateOf(item.isediting) }
    Row {
        Column {
            BasicTextField(value = editedname , onValueChange = {editedname=it} ) {

            }
           BasicTextField(value = editedquantity, onValueChange = {editedquantity=it}) {

           }
        }
        Button(onClick = {
            isediting=false
            oneditcomplete(editedname,editedquantity.toIntOrNull() ?: 1)
        }) {
            Text(text = "save")
        }
    }
}

