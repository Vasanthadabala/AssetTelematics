package com.example.assettelematics.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assettelematics.R
import com.example.assettelematics.components.BarcodeScanner
import com.example.assettelematics.data.ktor.VehicleConfigViewModel
import io.ktor.util.InternalAPI


@InternalAPI
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddVechileScreen() {
   Scaffold(
      topBar = { TopBar(name = "Add Vechile")}
   ) {
      Column(
         Modifier
            .fillMaxSize()
            .padding(top = 80.dp)
            .background(Color(0XFFF9F9F9))
      ) {
         AddVechileScreenComponent()
      }
   }
}

@InternalAPI
@Composable
fun AddVechileScreenComponent() {

   val context = LocalContext.current
   val keyboardController = LocalSoftwareKeyboardController.current
   val viewModel: VehicleConfigViewModel = viewModel()

   val isScanning = remember { mutableStateOf(false) }



   var imei by remember { mutableStateOf("") }
   var tagName by remember { mutableStateOf("") }
   var registrationNumber by remember { mutableStateOf("") }
   var brand by remember { mutableStateOf("") }
   var model by remember { mutableStateOf("") }
   var year by remember { mutableStateOf("") }
   var fuelType by remember { mutableStateOf("") }
   var capacity by remember { mutableStateOf("") }

   var ownership by remember { mutableStateOf("Owned") }
   var role by remember { mutableStateOf("Driver") }
   val roles = listOf("Driver", "Passenger")


   val vehicleConfigs by viewModel.getVehicleConfigs().observeAsState(emptyList())
   val vehicleTypes = vehicleConfigs.map { it.vehicle_type }.distinct()
   val vehicleMakes = vehicleConfigs.map { it.vehicle_make }.distinct()
   val manufactureYears = vehicleConfigs.map { it.manufacture_year }.distinct()
   val fuelTypes = vehicleConfigs.map { it.fuel_type }.distinct()
   val capacities = vehicleConfigs.map { it.vehicle_capacity }.distinct()

   // State variables for each of the six additional digits
   val imeiDigit1 = remember { mutableStateOf("") }
   val imeiDigit2 = remember { mutableStateOf("") }
   val imeiDigit3 = remember { mutableStateOf("") }
   val imeiDigit4 = remember { mutableStateOf("") }
   val imeiDigit5 = remember { mutableStateOf("") }
   val imeiDigit6 = remember { mutableStateOf("") }

   // FocusRequesters for each TextField
   val focusRequesters = List(6) { FocusRequester() }

   // Call the fetch function to load the data when the screen is first displayed
   LaunchedEffect(Unit) {
      try {
         viewModel.fetchAndStoreVehicleConfig()
      } catch (e: Exception) {
         Log.e("AddVehicleScreenComponent", "Error fetching vehicle config", e)
         Toast.makeText(context, "Error fetching vehicle config", Toast.LENGTH_LONG).show()
      }
   }

   // Log the fetched data for debugging
   LaunchedEffect(vehicleConfigs) {
      Log.d("AddVechileScreenComponent", "Observed vehicle configs: $vehicleConfigs")
   }



   Column(
      modifier = Modifier
         .padding(10.dp)
         .fillMaxSize()
   ) {
      Column(
         modifier = Modifier
            .fillMaxHeight()
         .verticalScroll(rememberScrollState())
         .weight(1f)
      ) {
         Text(
            text = "Enter last 6-digits of IMEI",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 10.dp)
         )

         Row(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
         ) {
            createDigitField(imeiDigit1, focusRequesters[0], focusRequesters[1])
            createDigitField(imeiDigit2, focusRequesters[1], focusRequesters[2])
            createDigitField(imeiDigit3, focusRequesters[2], focusRequesters[3])
            createDigitField(imeiDigit4, focusRequesters[3], focusRequesters[4])
            createDigitField(imeiDigit5, focusRequesters[4], focusRequesters[5])
            createDigitField(imeiDigit6, focusRequesters[5])
         }


         Text(
            text = "IMEI",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 10.dp)
         )

         OutlinedTextField(
            value = imei, onValueChange = { imei = it },
            singleLine = true,
            placeholder = { Text(text = "") },
            modifier = Modifier
               .fillMaxWidth()
               .background(Color.White)
               .padding(top = 10.dp),
            shape = RoundedCornerShape(14),
            keyboardOptions = KeyboardOptions.Default.copy(
               imeAction = ImeAction.Done,
               keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
               onDone = { keyboardController?.hide() }
            ),
            textStyle = TextStyle(
               fontWeight = FontWeight.W500,
               fontSize = 18.sp
            ),
            trailingIcon = {
               Box(
                  modifier = Modifier.padding(end = 10.dp),
                  contentAlignment = Alignment.Center
               ) {
                  Icon(
                     imageVector = Icons.Default.QrCodeScanner,
                     contentDescription = "",
                     modifier = Modifier
                        .size(32.dp)
                        .clickable { isScanning.value= true }
                  )
               }
            },
            colors = TextFieldDefaults.colors(
               focusedIndicatorColor = Color.DarkGray,
               unfocusedIndicatorColor = Color.DarkGray,
               focusedContainerColor = Color.White,
               unfocusedContainerColor = Color.White,
               cursorColor = Color.Black
            )
         )

         // Barcode scanner composable
         BarcodeScanner(
            onScanResult = { scanResult ->
               imei = scanResult
               if (scanResult.length >= 6) {
                  imeiDigit1.value = scanResult[scanResult.length - 6].toString()
                  imeiDigit2.value = scanResult[scanResult.length - 5].toString()
                  imeiDigit3.value = scanResult[scanResult.length - 4].toString()
                  imeiDigit4.value = scanResult[scanResult.length - 3].toString()
                  imeiDigit5.value = scanResult[scanResult.length - 2].toString()
                  imeiDigit6.value = scanResult[scanResult.length - 1].toString()
               }
               Toast.makeText(context, "Scanned: $scanResult", Toast.LENGTH_SHORT).show()
            },
            onPermissionDenied = {
               Toast.makeText(context, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show()
            },
            isScanning = isScanning
         )


         Text(
            text = "Vechile Details",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 10.dp)
         )

         OutlinedTextField(
            value = tagName, onValueChange = { tagName = it },
            singleLine = true,
            placeholder = { Text(text = "Tag Name", fontSize = 14.sp) },
            modifier = Modifier
               .fillMaxWidth()
               .background(Color.White)
               .padding(top = 10.dp),
            shape = RoundedCornerShape(14),
            keyboardOptions = KeyboardOptions.Default.copy(
               imeAction = ImeAction.Done,
               keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
               onDone = { keyboardController?.hide() }
            ),
            textStyle = TextStyle(
               fontWeight = FontWeight.W500,
               fontSize = 18.sp
            ),
            colors = TextFieldDefaults.colors(
               focusedIndicatorColor = Color.DarkGray,
               unfocusedIndicatorColor = Color.DarkGray,
               focusedContainerColor = Color.White,
               unfocusedContainerColor = Color.White,
               cursorColor = Color.Black
            )
         )

         OutlinedTextField(
            value = registrationNumber, onValueChange = { registrationNumber = it },
            singleLine = true,
            placeholder = { Text(text = "Registration Number", fontSize = 14.sp) },
            modifier = Modifier
               .fillMaxWidth()
               .background(Color.White)
               .padding(top = 10.dp),
            shape = RoundedCornerShape(12),
            keyboardOptions = KeyboardOptions.Default.copy(
               imeAction = ImeAction.Done,
               keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
               onDone = { keyboardController?.hide() }
            ),
            textStyle = TextStyle(
               fontWeight = FontWeight.W500,
               fontSize = 18.sp
            ),
            colors = TextFieldDefaults.colors(
               focusedIndicatorColor = Color.DarkGray,
               unfocusedIndicatorColor = Color.DarkGray,
               focusedContainerColor = Color.White,
               unfocusedContainerColor = Color.White,
               cursorColor = Color.Black
            )
         )

         Text(
            text = "Vechile Type",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 10.dp)
         )

         LazyRow {
            items(vehicleTypes.size) { index ->
               Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier.fillMaxWidth().padding(5.dp)
               ) {
                  Image(
                     painter = painterResource(id = R.drawable.truck),
                     contentDescription = null,
                     modifier = Modifier
                        .size(40.dp),
                     contentScale = ContentScale.FillWidth
                  )
                  Text(
                     text = vehicleTypes[index],
                     fontSize = 14.sp,
                     fontWeight = FontWeight.Bold,
                     modifier = Modifier.padding(5.dp)
                  )
               }
            }
         }

         Row(
            modifier = Modifier
               .padding(top = 10.dp)
               .fillMaxWidth()
         ) {
            OutlinedTextFieldWithDropdown(
               value = brand,
               onValueChange = { brand = it },
               placeholder = "Brand",
               options = vehicleMakes
            )

            Spacer(modifier = Modifier.width(5.dp))

            OutlinedTextFieldWithDropdown(
               value = model,
               onValueChange = { model = it },
               placeholder = "Model",
               options = vehicleMakes
            )
         }


         OutlinedTextFieldWithDropdown(
            value = year,
            onValueChange = { year = it },
            placeholder = "Enter Year",
            options = manufactureYears
         )

         OutlinedTextFieldWithDropdown(
            value = fuelType,
            onValueChange = { fuelType = it },
            placeholder = "Enter Fuel Type",
            options = fuelTypes
         )

         OutlinedTextFieldWithDropdown(
            value = capacity,
            onValueChange = { capacity = it },
            placeholder = "Enter Capacity",
            options = capacities
         )

         Text(
            text = "Ownership",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 10.dp)
         )

         OwnershipRadioButtons(
            options = listOf("Own", "Rent"),
            selectedOption = ownership,
            onOptionSelected = { ownership = it }
         )

         OutlinedTextFieldWithDropdown(
            value = role,
            onValueChange = { role = it },
            placeholder = "Select Role",
            options = roles
         )
      }
      Spacer(modifier = Modifier.height(10.dp))

      Button(
         onClick = {
            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
         },
         colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
         modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
            .height(50.dp),
         shape = RoundedCornerShape(12.dp)
      ) {
         Text(
            text = "Add",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
         )
      }
   }
}


@Composable
fun OutlinedTextFieldWithDropdown(
   value: String,
   onValueChange: (String) -> Unit,
   placeholder: String,
   options: List<String>,
   modifier: Modifier = Modifier,
   trailingIcon: @Composable (() -> Unit)? = null
) {

   var isDropdownMenuVisible by remember { mutableStateOf(false) }
   var selectedOption by remember { mutableStateOf(value) }

   Box(
      modifier = modifier
         .fillMaxWidth()
         .padding(top = 10.dp)
   ) {

      OutlinedTextField(
         value = selectedOption,
         onValueChange = {
            selectedOption = it
            onValueChange(it)
         },
         singleLine = true,
         readOnly = true,
         placeholder = { Text(text = placeholder, fontSize = 14.sp) },
         modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
         shape = RoundedCornerShape(12),
         textStyle = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 18.sp
         ),
         colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.DarkGray,
            unfocusedIndicatorColor = Color.DarkGray,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = Color.Transparent
         ),
         trailingIcon = trailingIcon ?: {
            IconButton(onClick = { isDropdownMenuVisible = !isDropdownMenuVisible }) {
               Icon(
                  imageVector = Icons.Default.ArrowDropDown,
                  contentDescription = null,
                  tint = Color(0xff0C2D48)
               )
            }
         },
      )

      DropdownMenu(
         expanded = isDropdownMenuVisible,
         onDismissRequest = { isDropdownMenuVisible = false },
         modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
      ) {
         options.forEach { option ->
            DropdownMenuItem(onClick = {
               selectedOption = option
               isDropdownMenuVisible = false
               onValueChange(option)
            }) {
               Text(
                  text = option,
                  color = Color(0xff0C2D48),
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold
               )
            }
         }
      }
   }
}

@Composable
fun createDigitField(
   value: MutableState<String>,
   focusRequester: FocusRequester,
   nextFocusRequester: FocusRequester? = null
) {
   OutlinedTextField(
      value = value.value,
      onValueChange = { newValue ->
         if (newValue.length <= 1) value.value = newValue
         if (newValue.length == 1) {
            nextFocusRequester?.requestFocus()
         }
      },
      singleLine = true,
      modifier = Modifier
         .width(50.dp)
         .background(Color.White)
         .focusRequester(focusRequester),
      shape = RoundedCornerShape(14),
      keyboardOptions = KeyboardOptions.Default.copy(
         imeAction = if (nextFocusRequester != null) ImeAction.Next else ImeAction.Done,
         keyboardType = KeyboardType.Text
      ),
      textStyle = TextStyle(
         fontWeight = FontWeight.W500,
         fontSize = 18.sp,
         textAlign = TextAlign.Center
      ),
      colors = TextFieldDefaults.colors(
         focusedIndicatorColor = Color.DarkGray,
         unfocusedIndicatorColor = Color.DarkGray,
         focusedContainerColor = Color.White,
         unfocusedContainerColor = Color.White,
         cursorColor = Color.Black
      )
   )
}

@Composable
fun OwnershipRadioButtons(
   options: List<String>,
   selectedOption: String,
   onOptionSelected: (String) -> Unit
) {
   Row {
      options.forEach { option ->
         Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)
         ) {
            RadioButton(
               selected = option == selectedOption,
               onClick = { onOptionSelected(option) }
            )
            Text(
               text = option,
               fontSize = 16.sp,
               fontWeight = FontWeight.Medium,
               modifier = Modifier.padding(start = 5.dp)
            )
         }
      }
   }
}