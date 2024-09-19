package com.app.cinerma.design.peliculas;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.app.cinerma.R;
import com.app.cinerma.design.peliculas.adapters.MoviesImageAdapter;
import com.app.cinerma.design.peliculas.entities.Movie;
import com.app.cinerma.design.peliculas.entities.MovieCard;
import com.app.cinerma.design.peliculas.services.MovieApi;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import com.app.cinerma.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment {
    //imganes y boton para ver detalles
    private RecyclerView recyclerView;
    // Adaptador personalizado para los card
    private MoviesImageAdapter moviesAdapter;
    //Usamos nuetro servicio api
    private MovieApi movieApi;
    //Entidad
    private List<Movie> movies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar(crear) el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        // Configurar el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        // Inicializar Retrofit y la interfaz API
        movieApi = RetrofitClient.getRetrofitInstance().create(MovieApi.class);

        // Llamar a la API para obtener las imagenes de cada una
        fetchMovies();

        return view;
    }

    private void fetchMovies() {
        Call<List<Movie>> call = movieApi.getMovies();
        call.enqueue(new Callback<List<Movie>>() {

            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body();
                    // Pasar el contexto al adaptador junto con la lista de películas
                    moviesAdapter = new MoviesImageAdapter(movies,requireContext());
                    recyclerView.setAdapter(moviesAdapter);
                } else {
                    Toast.makeText(getContext(), "Error al obtener datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}