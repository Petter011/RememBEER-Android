package com.petter.remembeer.screens


/*@Composable
fun ReceivedBeerScreen(navController: NavHostController, viewModel: BeerViewModel
) {
    val scannedResult = remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scannedText = result.data?.getStringExtra("SCAN_RESULT")
            scannedResult.value = scannedText
        }
    }

    Background()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(text = "Received Beer")
        ScannedBeerListView(navController, viewModel)

    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Column(
            )
             {
                scannedResult.value?.let { jsonContent ->
                    DisplayBeerInfo(jsonContent, viewModel, navController) // Pass the scanned JSON content to the DisplayBeerInfo function
                }
            }
        ElevatedButton(
            onClick = {
                val intent = Intent(context, ScanActivity::class.java)
                launcher.launch(intent)
                      },
            modifier = Modifier
                .padding(36.dp)
                .size(width = 230.dp, height = 70.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text(
                text = "Scan QR Code",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Yellow
                )
            )
        }
    }
}

@Composable
fun ScannedBeerListView(
    navController: NavHostController,
    viewModel: BeerViewModel
) {
    val allBeerList by viewModel.allBeerList.collectAsState(initial = mutableListOf())


    Column {
        Spacer(modifier = Modifier.height(20.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f)
        ) {
            items(allBeerList) { beerTypeWithBeers ->
                val beerList = beerTypeWithBeers.beerList

                beerList.forEach { beer ->
                Card(
                    modifier = Modifier
                        .clickable {
                            navController.navigate("${NavigationItem.BeerType.route}/${beer.uid}")
                        }
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = beer.name!!,
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.Yellow,
                                textAlign = TextAlign.Center,
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}



@Composable
fun DisplayBeerInfo(jsonString: String, viewModel: BeerViewModel, navController: NavController) {
    val scannedBeer = parseJsonFromString(jsonString)
    val context = LocalContext.current
    val imageUrl = scannedBeer?.image // Convert imageUrl to Uri
    val uri = imageUrl?.let { Uri.parse(it) } // Convert String URL to Uri


    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.LightGray)
    ) {

        if (scannedBeer != null) {
            Text(
                text = " ${scannedBeer.name}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Type: ${scannedBeer.name}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Note: ${scannedBeer.note}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Rating: ${scannedBeer.rating}",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            //BeerImage(url = scannedBeer.image)

        } else {
            Text(
                text = "Error parsing beer information",
                textAlign = TextAlign.Center
            )
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(bottom = 20.dp)
        ) {
            ElevatedButton(onClick = {
                if (scannedBeer != null) {
                    //viewModel.addBeer(scannedBeer, uri, isScanned = true)
                    Log.d("ScannedBeerSheet", "Scanned beer added: $scannedBeer")
                    navController.navigate(route = NavigationItem.ReceivedBeer.route)
                }
            }) {
                Text(text = "Save")
            }
        }
    }
}
@Composable
fun BeerImage(url: String?, placeholder: Painter? = null) {
    val painter = rememberAsyncImagePainter(model = url)

    Image(
        painter = painter,
        contentDescription = "Beer Image",
        modifier = Modifier.size(130.dp),
        contentScale = ContentScale.FillBounds,
        alignment = Alignment.Center,
    )
}

class ScanActivity : AppCompatActivity() {
    private lateinit var barcodeView: DecoratedBarcodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcodeView = DecoratedBarcodeView(this)
        setContentView(barcodeView)

        barcodeView.decodeContinuous(callback)
    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            result?.let {
                val scannedText = result.text
                if (!scannedText.isNullOrEmpty()) {
                    val intent = Intent().apply {
                        putExtra("SCAN_RESULT", scannedText) // Pass scanned text directly as result data
                    }
                    setResult(Activity.RESULT_OK, intent)
                }
            }
            finish()
        }

        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
}
*/

