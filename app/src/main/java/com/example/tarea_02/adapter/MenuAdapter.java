package com.example.tarea_02.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarea_02.R;
import com.example.tarea_02.db.CarritoDAO;
import com.example.tarea_02.model.MenuHeaderModel;
import com.example.tarea_02.model.MenuItemModel;

import java.util.List;

/**
 * Adaptador del RecyclerView que maneja tanto ítems tipo encabezado (categorías)
 * como productos reales dentro del menú.
 *
 * items = Lista mezclada de MenuHeaderModel + MenuItemModel (para UI)
 * productos = Lista únicamente de productos (para actualizar cantidades)
 *
 * Este adaptador también interactúa directamente con el DAO del carrito para
 * actualizar cantidades y notificar cambios.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private static final String TAG = "MenuAdapter";

    // Identificadores de tipo de ítem
    private static final int TYPE_PRODUCTO = 0;
    private static final int TYPE_HEADER = 1;

    private final List<Object> items;              // Lista para UI (headers + productos)
    private final List<MenuItemModel> productos;   // Lista real de productos
    private final Context context;
    private final CarritoDAO carritoDAO;

    /** Listener para actualizar el badge del carrito en el Activity */
    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }

    private OnCartUpdatedListener cartListener;

    public void setOnCartUpdatedListener(OnCartUpdatedListener listener) {
        this.cartListener = listener;
    }

    public MenuAdapter(Context context, List<Object> items, List<MenuItemModel> productos, CarritoDAO dao) {
        this.context = context;
        this.items = items;
        this.productos = productos;
        this.carritoDAO = dao;
        Log.d(TAG, "MenuAdapter: Adaptador inicializado con " + items.size() + " elementos.");
    }

    @Override
    public int getItemViewType(int position) {
        return (items.get(position) instanceof MenuHeaderModel)
                ? TYPE_HEADER
                : TYPE_PRODUCTO;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: Creando ViewHolder para tipo = " + viewType);

        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
            return new ViewHolder(view, TYPE_HEADER);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
            return new ViewHolder(view, TYPE_PRODUCTO);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (holder.type == TYPE_HEADER) {
            MenuHeaderModel header = (MenuHeaderModel) items.get(position);

            Log.d(TAG, "onBindViewHolder: Mostrando HEADER -> " + header.titulo);

            holder.headerTitle.setText(header.titulo);
            return;
        }

        // Producto real
        MenuItemModel model = (MenuItemModel) items.get(position);

        Log.d(TAG, "onBindViewHolder: Producto -> " + model.nombre);

        holder.img.setImageResource(model.imageRes);
        holder.nombre.setText(model.nombre);
        holder.descripcion.setText(model.descripcion);
        holder.precio.setText(model.precio);

        // Obtiene la cantidad actual desde la BD
        int cantidad = carritoDAO.obtenerCantidadProducto(model.nombre);
        model.cantidad = cantidad;
        holder.cantidad.setText(String.valueOf(cantidad));

        Log.d(TAG, "Cantidad actual de " + model.nombre + " = " + cantidad);

        // Botón aumentar
        holder.btnIncrement.setOnClickListener(v -> {
            Log.d(TAG, "btnIncrement: Agregando -> " + model.nombre);
            carritoDAO.agregarProducto(model.nombre);
            actualizarCantidad(holder, model);
        });

        // Botón disminuir
        holder.btnDecrement.setOnClickListener(v -> {
            Log.d(TAG, "btnDecrement: Restando -> " + model.nombre);
            carritoDAO.restarProducto(model.nombre);
            actualizarCantidad(holder, model);
        });
    }

    /**
     * Actualiza la cantidad mostrada y notifica al listener para actualizar el badge del carrito.
     */
    private void actualizarCantidad(ViewHolder holder, MenuItemModel model) {

        int nuevaCantidad = carritoDAO.obtenerCantidadProducto(model.nombre);
        model.cantidad = nuevaCantidad;

        Log.d(TAG, "actualizarCantidad: Nueva cantidad de " + model.nombre + " = " + nuevaCantidad);

        holder.cantidad.setText(String.valueOf(nuevaCantidad));

        if (cartListener != null) {
            Log.d(TAG, "actualizarCantidad: Notificando actualización al listener.");
            cartListener.onCartUpdated();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Actualiza la cantidad de todos los productos (NO de headers).
     * Se usa cuando se regresa desde el carrito.
     */
    public void refreshQuantities() {
        Log.d(TAG, "refreshQuantities: Refrescando cantidades de productos...");

        for (MenuItemModel p : productos) {
            p.cantidad = carritoDAO.obtenerCantidadProducto(p.nombre);
            Log.d(TAG, "refreshQuantities: " + p.nombre + " = " + p.cantidad);
        }

        notifyDataSetChanged();
    }

    /**
     * ViewHolder que puede representar un header o un producto.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        int type;

        // PRODUCTO UI
        ImageView img;
        TextView nombre, descripcion, precio, cantidad, btnIncrement, btnDecrement;

        // HEADER UI
        TextView headerTitle;

        public ViewHolder(@NonNull View itemView, int type) {
            super(itemView);
            this.type = type;

            Log.d(TAG, "ViewHolder: Inicializando para tipo = " + type);

            if (type == TYPE_HEADER) {
                headerTitle = itemView.findViewById(R.id.tvHeader);
            } else {
                img = itemView.findViewById(R.id.imgProducto);
                nombre = itemView.findViewById(R.id.tvNombre);
                descripcion = itemView.findViewById(R.id.tvDescripcion);
                precio = itemView.findViewById(R.id.tvPrecio);
                cantidad = itemView.findViewById(R.id.tvCantidad);
                btnIncrement = itemView.findViewById(R.id.btnIncrement);
                btnDecrement = itemView.findViewById(R.id.btnDecrement);
            }
        }
    }
}
