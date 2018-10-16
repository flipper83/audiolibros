package com.example.audiolibros;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Jesús Tomás on 27/01/2016.
 */
public class FilterBookAdapter extends
  RecyclerView.Adapter<FilterBookAdapter.ViewHolder> {

  private View.OnClickListener onClickListener;
  private View.OnLongClickListener onLongClickListener;

  private final List<Libro> listBooks = new ArrayList<>();

  private Vector<Libro> originalListOfBooks = new Vector<>();
  private Vector<Integer> filterIndex = new Vector<>();

  private String searchKeyword = "";
  private String gender = "";
  private boolean newBook = false;
  private boolean read = false;

  public FilterBookAdapter(Vector<Libro> originBooks) {
    originalListOfBooks = originBooks;
    recalculateFilter();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View rootItem = inflater.inflate(R.layout.elemento_selector, null);
    rootItem.setOnClickListener(onClickListener);
    rootItem.setOnLongClickListener(onLongClickListener);
    return new ViewHolder(rootItem);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int pos) {
    Libro book = listBooks.get(pos);

    renderThumbnail(holder, book);
    renderTitle(holder, book);
  }

  private void renderTitle(ViewHolder holder, Libro book) {
    holder.titleView.setText(book.titulo);

    Palette palette = extractPalette(holder, book);
    holder.itemView.setBackgroundColor(palette.getLightMutedColor(0));
    holder.titleView.setBackgroundColor(palette.getLightVibrantColor(0));
  }

  @NonNull
  private Palette extractPalette(ViewHolder holder, Libro book) {
    Bitmap bitmap = BitmapFactory.decodeResource(holder.itemView.getResources(),
      book.recursoImagen);
    return Palette.from(bitmap).generate();
  }

  private void renderThumbnail(ViewHolder holder, Libro book) {
    holder.thumbView.setImageResource(book.recursoImagen);
  }

  @Override
  public int getItemCount() {
    return listBooks.size();
  }

  public void setOnItemClickListener(View.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  public void setOnItemLongClickListener(View.OnLongClickListener
                                           onLongClickListener) {
    this.onLongClickListener = onLongClickListener;
  }

  public void setSearchKeyword(String searchKeyword) {
    this.searchKeyword = searchKeyword.toLowerCase();
    recalculateFilter();
  }

  public void setGender(String gender) {
    this.gender = gender;
    recalculateFilter();
  }

  public void setNewBook(boolean newBook) {
    this.newBook = newBook;
    recalculateFilter();
  }

  public void setRead(boolean read) {
    this.read = read;
    recalculateFilter();
  }

  public void recalculateFilter() {
    listBooks = new Vector<>();
    filterIndex = new Vector<>();
    for (int i = 0; i < originalListOfBooks.size(); i++) {
      Libro book = originalListOfBooks.elementAt(i);
      if ((matchTitle(book) || matchAuthor(book))
        && matchGender(book)
        && checkIsNew(book)
        && checkIsRead(book)) {
        listBooks.add(book);
        filterIndex.add(i);
      }
    }
  }

  private boolean checkIsRead(Libro book) {
    return !read || (read && book.leido);
  }

  private boolean checkIsNew(Libro book) {
    return !newBook || (newBook && book.novedad);
  }

  private boolean matchGender(Libro book) {
    return book.genero.startsWith(gender);
  }

  private boolean matchAuthor(Libro book) {
    return book.autor.toLowerCase().contains(searchKeyword);
  }

  private boolean matchTitle(Libro book) {
    return book.titulo.toLowerCase().contains(searchKeyword);
  }

  public Libro getItem(int postion) {
    return originalListOfBooks.elementAt(filterIndex.elementAt(postion));
  }

  public long getItemId(int position) {
    return filterIndex.elementAt(position);
  }

  public void remove(int position) {
    originalListOfBooks.remove((int) getItemId(position));
    recalculateFilter();
  }

  public void insert(Libro book) {
    originalListOfBooks.add(0, book);
    recalculateFilter();
  }

  public void reset() {
    searchKeyword = "";
    gender = "";
    newBook = false;
    read = false;
  }

  @TestOnly
  public void setLibros(Vector<Libro> libros) {
    originalListOfBooks = libros;
    recalculateFilter();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView thumbView;
    public TextView titleView;

    public ViewHolder(View itemView) {
      super(itemView);
      thumbView = itemView.findViewById(R.id.portada);
      thumbView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      titleView = itemView.findViewById(R.id.titulo);
    }
  }
}
