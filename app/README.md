# Proyecto Restaurante App

Aplicación Android que permite a los usuarios visualizar un menú, agregar productos a un carrito, realizar pedidos y consultar su historial de compras.

## Funcionalidades

- Visualización de menú con categorías (comidas, bebidas, extras).
- Agregar, quitar o eliminar productos del carrito.
- Cálculo automático del subtotal.
- Registro de pedidos con fecha y total.
- Historial de compras almacenado en SQLite.
- Drawer Navigation (menú lateral) para navegar entre secciones.

## Tecnologías utilizadas

- **Android Studio**
- **Java**
- **SQLite** (base de datos local)
- **RecyclerView** para manejar lista dinámica de productos
- **Adapter + Model** para manejo de datos
- **DAO (Data Access Object)** para operaciones con la base de datos

## Estructura principal

- `MenuActivity` – pantalla principal del menú.
- `CarritoActivity` – carrito de compras.
- `HistorialActivity` – historial de pedidos previos.
- `MenuAdapter` – adaptador para mostrar categorías y productos.
- `CarritoDAO` – manejo de BD para carrito y pedidos.
- `DatabaseHelper` – creación de tablas y mantenimiento de la base de datos.

## Autor
Proyecto desarrollado como práctica de Android.
