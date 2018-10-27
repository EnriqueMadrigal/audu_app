package audu.app.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import audu.app.R;
import audu.app.common;
import audu.app.models.Libro_Class;
import audu.app.utils.IViewHolderClick;
import com.squareup.picasso.Picasso;


public class bookGridAdapter extends RecyclerView.Adapter<bookGridAdapter.ViewHolder>
{
    private Context _context;
    private ArrayList<Libro_Class> _items;
    private IViewHolderClick _listener;

    public bookGridAdapter( Context context, ArrayList<Libro_Class> items, IViewHolderClick listener )
    {
        _context = context;
        _items = items;
        _listener = listener;
    }



    @Override
    public int getItemCount()
    {
        return _items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType )
    {
        View view = LayoutInflater.from( _context ).inflate( R.layout.item_book, parent, false );
        ViewHolder viewHolder = new ViewHolder( view, new IViewHolderClick()
        {
            @Override
            public void onClick( int position )
            {
                if( _listener != null )
                    _listener.onClick( position );
            }
        } );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position )
    {
        Libro_Class item = _items.get( position );



       // if( item.getResource() != -1 )
       // {
       //     holder.getIconView().setImageResource( item.getResource() );
      //  }



        if( item.get_portada().length() > 0 )
        {
            /*
            File f = new File( Common.getBaseDirectory(), String.format( "assets/html/%s", item.getIconMosaic() ) );

            Uri urifile = Uri.fromFile(f);


                int sizex = holder.getIconView().getLayoutParams().width;
                int sizey = holder.getIconView().getLayoutParams().height;

                if (f.exists()) {


                    holder.getIconView().setImageURI(urifile);
                    //int sizex = holder.getIconView().getLayoutParams().width;

                   // holder.getIconView().setAlpha(0.60f);
                   // holder.getIconView2().setImageResource(R.drawable.download_icon);
                   // holder.getIconView2().getLayoutParams().width = sizex;
                   // holder.getIconView2().getLayoutParams().height = sizey;
                   // holder.getIconView2().setAlpha(0.60f);
                   // holder.getIconView2().requestLayout();


                } else {
                    holder.getIconView().setImageResource(R.drawable.placeholder);

                    //holder.getIconView().setAlpha(1.0f);
                    //holder.getIconView2().setImageResource(android.R.color.transparent);
                    //holder.getIconView2().setImageResource(R.drawable.download_icon);
                    //holder.getIconView2().setAlpha(0.0f);


                    //holder.getIconView2().getLayoutParams().width = sizex;
                    //holder.getIconView2().getLayoutParams().height = sizey;
                    //holder.getIconView2().requestLayout();

                }




            //		}

*/

        }

        else if (item.get_idLibro() >0)
        {
            String curLink = common.API_URL_BASE + "getPortada.php?idLibro=" + String.valueOf(item.get_idLibro());

            if (curLink.length()>8) {
                Picasso.with(_context)
                        .load(curLink)
                        .placeholder(R.drawable.placeholder)
                        .into(holder.getIconView());
            }

        }

        else
        {
            //holder.getIconView().setImageResource( android.R.color.transparent );
            holder.getIconView().setImageResource( R.drawable.placeholder );
        }


        holder.setIndex( position );
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView _iconView;
        private int _index;
        private IViewHolderClick _listener;
        private View _parent;

        public ViewHolder( View view, IViewHolderClick listener )
        {
            super( view );

            _parent = view;
            view.setOnClickListener( this );
            _iconView = (ImageView) view.findViewById( R.id.imgIcon );
           _listener = listener;
        }

        public ImageView getIconView()
        {
            return _iconView;
        }


        public int getIndex()
        {
            return _index;
        }

        public void setIndex( int index )
        {
            _index = index;
        }

        @Override
        public void onClick(View v)
        {
            if( _listener != null )
                _listener.onClick( _index );
        }
    }


}
