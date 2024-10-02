package com.example.appgood

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var userListContainer: LinearLayout
    private lateinit var btnGetUser: Button
    private lateinit var btnLogin: Button
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private val client = OkHttpClient()
    private lateinit var userList: JSONArray // Guardaremos la lista de usuarios aquí después de la llamada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener referencias a los elementos de la interfaz
        userListContainer = findViewById(R.id.userListContainer)
        btnGetUser = findViewById(R.id.btnGetUser)
        btnLogin = findViewById(R.id.btnLogin)
        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)

        // Acción del botón para iniciar sesión
        btnLogin.setOnClickListener {
            // Intentar iniciar sesión con los datos ingresados
            attemptLogin()
        }

        // Acción del botón para obtener y mostrar los usuarios
        btnGetUser.setOnClickListener {
            // Llamar al método para obtener los usuarios
            getUsersFromService()
        }
    }

    // Método para hacer la solicitud HTTP y obtener la lista de usuarios
    private fun getUsersFromService() {
        // URL del servicio que devuelve la lista de usuarios
        val url = "http://54.236.89.141:8080/user"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                // Manejo básico para fallos en la conexión
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                val responseData = response.body?.string() ?: ""
                userList = JSONArray(responseData) // Guardar los usuarios obtenidos en una variable

                // Ejecutar en el hilo principal para mostrar los usuarios
                runOnUiThread {
                    showUsers(userList)
                }
            }
        })
    }

    // Método para mostrar la lista de usuarios en la pantalla
    @SuppressLint("SetTextI18n")
    private fun showUsers(userList: JSONArray) {
        // Limpiar la lista de usuarios antes de mostrar nuevos usuarios
        userListContainer.removeAllViews()

        // Iterar sobre el arreglo JSON y mostrar cada usuario
        for (i in 0 until userList.length()) {
            val user = userList.getJSONObject(i)
            val nombre = user.getString("nombre")
            val correo = user.getString("correo")
            val rut = user.getString("rut")
            val password = user.getString("contraseña")

            // Crear TextView para mostrar el nombre
            val nombreTextView = TextView(this).apply {
                text = "Nombre: $nombre"
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black))
                setPadding(0, 16, 0, 0)
            }

            // Crear TextView para mostrar el correo
            val correoTextView = TextView(this).apply {
                text = "Correo: $correo"
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black))
                setPadding(0, 8, 0, 0)
            }

            // Crear TextView para mostrar el RUT
            val rutTextView = TextView(this).apply {
                text = "RUT: $rut"
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black))
                setPadding(0, 8, 0, 0)
            }

            // Crear TextView para mostrar la contraseña
            val passwordTextView = TextView(this).apply {
                text = "Contraseña: $password"
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black))
                setPadding(0, 8, 0, 0)
            }

            // Agregar los TextViews al contenedor de la lista de usuarios
            userListContainer.apply {
                addView(nombreTextView)
                addView(correoTextView)
                addView(rutTextView)
                addView(passwordTextView)
            }
        }
    }

    // Método para intentar iniciar sesión con el RUT y la contraseña
    private fun attemptLogin() {
        val rut = editUsername.text.toString()
        val password = editPassword.text.toString()

        if (rut.isNotEmpty() && password.isNotEmpty()) {
            // Buscar el usuario en la lista
            val user = findUserByRutAndPassword(rut, password)
            if (user != null) {
                // Inicio de sesión exitoso, mostrar los datos del usuario
                showAuthenticatedUser(user)
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Por favor, ingrese RUT y contraseña", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para buscar al usuario por RUT y contraseña
    private fun findUserByRutAndPassword(rut: String, password: String): JSONObject? {
        for (i in 0 until userList.length()) {
            val user = userList.getJSONObject(i)
            if (user.getString("rut") == rut && user.getString("contraseña") == password) {
                return user // Retornar el usuario encontrado
            }
        }
        return null // No se encontró el usuario
    }

    // Método para mostrar los datos del usuario autenticado
    @SuppressLint("SetTextI18n")
    private fun showAuthenticatedUser(user: JSONObject) {
        // Limpiar la vista actual
        userListContainer.removeAllViews()

        val nombre = user.getString("nombre")
        val correo = user.getString("correo")

        // Crear TextView para mostrar el nombre
        val nombreTextView = TextView(this).apply {
            text = "Nombre: $nombre"
            textSize = 16f
            setTextColor(resources.getColor(android.R.color.black))
            setPadding(0, 16, 0, 0)
        }

        // Crear TextView para mostrar el correo
        val correoTextView = TextView(this).apply {
            text = "Correo: $correo"
            textSize = 16f
            setTextColor(resources.getColor(android.R.color.black))
            setPadding(0, 8, 0, 0)
        }

        // Agregar los TextViews al contenedor
        userListContainer.apply {
            addView(nombreTextView)
            addView(correoTextView)
        }

        // Mensaje de inicio de sesión exitoso
        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
    }
}
