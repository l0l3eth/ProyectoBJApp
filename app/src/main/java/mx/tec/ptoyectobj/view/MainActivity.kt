package mx.tec.ptoyectobj.view

import Inicio
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.tec.ptoyectobj.ui.theme.PtoyectoBJTheme
import mx.tec.ptoyectobj.viewmodel.AppVM
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appVM: AppVM by viewModels()
            PtoyectoBJTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "EntradaRoute" // Pantalla 1 es el inicio
                ) {
                    composable("EntradaRoute") {
                        Entrada(navController = navController)
                    }
                    composable("InicioRoute") {
                        Inicio(navController = navController) // Pantalla 2
                    }
                }
            }
        }
    }
}

//@Composable
//fun NavHostApp(navController: NavController, modifier: Modifier = Modifier){
//    NavHost(
//        navController = navController,
//        startDestination = "EntradaRoute",
//        modifier = modifier
//    ){
//        Entrada(navController = navController)
//
//        composable("InicioRoute"){
//            Inicio(navController = navController)
//        }
//
//    }
//}