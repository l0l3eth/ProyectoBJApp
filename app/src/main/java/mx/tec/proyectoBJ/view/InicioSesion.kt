package mx.tec.proyectoBJ.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.proyectoBJ.R
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.blanco
import mx.tec.ptoyectobj.degradado
import mx.tec.ptoyectobj.morado


@Composable
fun InicioSesion( onNavigateToRegistro: () -> Unit /*Logica de navegación*/,
                  appVM: AppVM
) {
    // Estados para los campos de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(morado)
    ) {
        // --- Contenido de la Pantalla ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            LogoYTextoGrande()

            Spacer(modifier = Modifier.height(48.dp))

            // Título "Nos alegra tenerte de regreso"
            Text(
                text = "Nos alegra tenerte de regreso",
                color = blanco,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Subtítulo
            Text(
                text = "Ingresa tu correo y contraseña para acceder a nuevas promociones",
                color = blanco.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 32.dp)
            )

            // --- Campos de Formulario ---

            // Campo de Correo
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Correo") },
                shape = RoundedCornerShape(28.dp),
                colors = outlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
                trailingIcon = {
                    val painter = if (passwordVisible)
                        painterResource(id = R.drawable.visibility)
                    else
                        painterResource(id = R.drawable.visibility_off)

                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painter,
                            contentDescription = description,
                            modifier = Modifier.size(24.dp) // Adjust size as needed
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(28.dp),
                colors = outlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Enlace "Olvidaste tu contraseña?"
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = blanco,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp)
                    .clickable {/* TODO Logica de contraseña*/  } //Navegación a PuntoPartida
                    .wrapContentWidth(Alignment.End)// Alinear a la derecha
            )

            //Enlace "¿Eres nuevo? Registrate"
            Text(
                text = "¿Eres nuevo? Regístrate",
                color = blanco,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, bottom = 32.dp)
                    .clickable { onNavigateToRegistro() }
                    .wrapContentWidth(Alignment.End) // Alinear a la derecha
            )

            // Botón "Iniciar sesión" (con degradado)
            Button(
                onClick = {appVM.iniciarSesion(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(degradado, RoundedCornerShape(28.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(28.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(degradado),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Iniciar sesión",
                        color = blanco,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// Función auxiliar para los colores de los campos de texto
@Composable
private fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = blanco.copy(alpha = 0.9f),
    unfocusedContainerColor = blanco.copy(alpha = 0.9f),
    focusedBorderColor = Color.Transparent,
    unfocusedBorderColor = Color.Transparent,
    focusedLabelColor = blanco,
    unfocusedLabelColor = morado.copy(alpha = 0.7f),
    focusedLeadingIconColor = morado,
    unfocusedLeadingIconColor = morado.copy(alpha = 0.7f),
    cursorColor = morado
)

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    InicioSesion( onNavigateToRegistro = { }, appVM = AppVM())
}