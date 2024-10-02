from flask import Flask, jsonify

app = Flask(__name__)

# Ruta que devuelve información de usuarios ficticios
@app.route('/user', methods=['GET'])
def get_user():
    user_info = [
        {
            "nombre": "Luis Toro Chavez",
            "correo": "luistoro1203@gmail.com",
            "rut": "123456789",
            "contraseña": "password123"
        },
        {
            "nombre": "Carlos Mendoza Pérez",
            "correo": "cmendoza@gmail.com",
            "rut": "112233445",
            "contraseña": "securepass456"
        },
        {
            "nombre": "Ana López Martínez",
            "correo": "ana.lopez@gmail.com",
            "rut": "998877665",
            "contraseña": "ana789"
        },
        {
            "nombre": "Jorge Campos Vidal",
            "correo": "jorge.campos@gmail.com",
            "rut": "554433221",
            "contraseña": "jorge1234"
        },
        {
            "nombre": "Laura Castillo Vargas",
            "correo": "laura.castillo@gmail.com",
            "rut": "667788990",
            "contraseña": "lauraPass!2023"
        }
    ]
    return jsonify(user_info)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)
