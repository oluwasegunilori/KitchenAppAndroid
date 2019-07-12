package com.example.changeme.kitchenapp.helper;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.changeme.kitchenapp.Interface.RecyclerItemTouchHelperListener;
import com.example.changeme.kitchenapp.ViewHolder.CartViewHolder;
import com.example.changeme.kitchenapp.ViewHolder.FavouriteViewHolder;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        if(listener!=null){
            listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {

        return super.convertToAbsoluteDirection(flags, layoutDirection);

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if(viewHolder instanceof CartViewHolder){
        View foreground = ((CartViewHolder) viewHolder).viewforeground;
        getDefaultUIUtil().clearView(foreground);
        }else if(viewHolder instanceof FavouriteViewHolder){
            View foreground = ((FavouriteViewHolder) viewHolder).viewforeground;
            getDefaultUIUtil().clearView(foreground);}


    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(viewHolder instanceof CartViewHolder){
        View foreground = ((CartViewHolder)viewHolder).viewforeground;
        getDefaultUIUtil().onDraw(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);}
        else if(viewHolder instanceof FavouriteViewHolder){
            View foreground = ((FavouriteViewHolder)viewHolder).viewforeground;
            getDefaultUIUtil().onDraw(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);
        }
      }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder!=null){

            if(viewHolder instanceof CartViewHolder){
                View foregroundview = ((CartViewHolder) viewHolder).viewforeground;
                getDefaultUIUtil().onSelected(foregroundview);
            }else if(viewHolder instanceof FavouriteViewHolder){
                View foregroundview = ((FavouriteViewHolder) viewHolder).viewforeground;
                getDefaultUIUtil().onSelected(foregroundview);}

        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
       if(viewHolder instanceof CartViewHolder){
           View foreground = ((CartViewHolder)viewHolder).viewforeground;
           getDefaultUIUtil().onDrawOver(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);

       }else if (viewHolder instanceof FavouriteViewHolder){
           View foreground = ((FavouriteViewHolder)viewHolder).viewforeground;
           getDefaultUIUtil().onDrawOver(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);

       }

    }
}
