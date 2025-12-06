package com.example.tarea_02.adapter;

import android.content.Context;
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

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private static final int TYPE_PRODUCTO = 0;
    private static final int TYPE_HEADER = 1;

    private final List<Object> items;              // lista para UI
    private final List<MenuItemModel> productos;   // lista real de productos
    private final Context context;
    private final CarritoDAO carritoDAO;

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
            holder.headerTitle.setText(header.titulo);
            return;
        }

        // Producto real
        MenuItemModel model = (MenuItemModel) items.get(position);

        holder.img.setImageResource(model.imageRes);
        holder.nombre.setText(model.nombre);
        holder.descripcion.setText(model.descripcion);
        holder.precio.setText(model.precio);

        int cantidad = carritoDAO.obtenerCantidadProducto(model.nombre);
        model.cantidad = cantidad;
        holder.cantidad.setText(String.valueOf(cantidad));

        holder.btnIncrement.setOnClickListener(v -> {
            carritoDAO.agregarProducto(model.nombre);
            actualizarCantidad(holder, model);
        });

        holder.btnDecrement.setOnClickListener(v -> {
            carritoDAO.restarProducto(model.nombre);
            actualizarCantidad(holder, model);
        });
    }

    private void actualizarCantidad(ViewHolder holder, MenuItemModel model) {
        int nuevaCantidad = carritoDAO.obtenerCantidadProducto(model.nombre);
        model.cantidad = nuevaCantidad;
        holder.cantidad.setText(String.valueOf(nuevaCantidad));

        if (cartListener != null) cartListener.onCartUpdated();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** REFRESH solo de productos, NO headers */
    public void refreshQuantities() {
        for (MenuItemModel p : productos) {
            p.cantidad = carritoDAO.obtenerCantidadProducto(p.nombre);
        }
        notifyDataSetChanged();
    }

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
