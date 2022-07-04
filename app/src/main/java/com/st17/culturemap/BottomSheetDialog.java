package com.st17.culturemap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.st17.culturemap.objects.ImageRVAdapter;
import com.st17.culturemap.objects.PlaceObject;

import java.util.List;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    public PlaceObject object;

    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;

    FloatingActionButton moreFab, likeFab, addToWantFab, addToVisitFab;
    boolean isFABsVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        TextView place_name = v.findViewById(R.id.textView_place_name);
        place_name.setText(object.name);

        TextView place_description = v.findViewById(R.id.place_description);
        place_description.setText(object.description);

        // RV
        RecyclerView recyclerView = v.findViewById(R.id.BottomSheet_RV);
        RecyclerViewLayoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        ImageRVAdapter rvAdapter = new ImageRVAdapter(inflater, object.imageURLs);
        HorizontalLayout = new LinearLayoutManager(v.getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(rvAdapter);

        FirebaseApp.initializeApp(v.getContext());

        //
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // FAB
        moreFab = v.findViewById(R.id.fab_more);
        likeFab = v.findViewById(R.id.fab_like);
        addToWantFab = v.findViewById(R.id.fab_addToPath);
        addToVisitFab = v.findViewById(R.id.fab_visited);

        if(currentUser!=null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference dbUser = db.collection("users").document(currentUser.getUid());

            dbUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        List<String> favourites = (List<String>) document.get("favourites");

                        if(favourites.contains(object.name)){
                            likeFab.setImageResource(R.drawable.ic_heart_on);
                        }
                        else {
                            likeFab.setImageResource(R.drawable.ic_baseline_heart_broken_24);
                        }

                        List<String> wantVisit = (List<String>) document.get("wantVisit");

                        if(wantVisit.contains(object.name)){
                            addToVisitFab.setImageResource(R.drawable.ic_baseline_bookmark_24);
                        }
                        else {
                            addToWantFab.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                        }

                        List<String> visited = (List<String>) document.get("visited");

                        if(visited.contains(object.name)){
                            addToVisitFab.setImageResource(R.drawable.ic_baseline_done_on);
                        }
                        else {
                            addToVisitFab.setImageResource(R.drawable.ic_baseline_done_24);
                        }

                    }
                }
            });

        }

        likeFab.setVisibility(View.GONE);
        addToWantFab.setVisibility(View.GONE);
        addToVisitFab.setVisibility(View.GONE);
        isFABsVisible = false;

        moreFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABsVisible) {
                    likeFab.show();
                    addToWantFab.show();
                    addToVisitFab.show();

                    isFABsVisible = true;
                } else {
                    likeFab.hide();
                    addToWantFab.hide();
                    addToVisitFab.hide();

                    isFABsVisible = false;
            }
        }});

        likeFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                        if(currentUser!=null){
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference dbUser = db.collection("users").document(currentUser.getUid());

                            dbUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();

                                        List<String> favourites = (List<String>) document.get("favourites");

                                        if(favourites.contains(object.id)){
                                            favourites.remove(object.id);
                                            Toast.makeText(v.getContext(), "Удалено из любимого", Toast.LENGTH_SHORT).show();
                                            likeFab.setImageResource(R.drawable.ic_baseline_heart_broken_24);
                                        }
                                        else{
                                            likeFab.setImageResource(R.drawable.ic_heart_on);
                                            favourites.add(object.id);
                                            Toast.makeText(v.getContext(), "Добавлено в любимое", Toast.LENGTH_SHORT).show();
                                        }
                                        String TAG = "updateList";

                                        dbUser.update("favourites", favourites).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(v.getContext(), "Войдите в аккаунт", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


        addToWantFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                        if(currentUser!=null){
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference dbUser = db.collection("users").document(currentUser.getUid());

                            dbUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();

                                        List<String> wantVisit = (List<String>) document.get("wantVisit");

                                        if(wantVisit.contains(object.id)){
                                            wantVisit.remove(object.id);
                                            Toast.makeText(v.getContext(), "Удалено из интересного", Toast.LENGTH_SHORT).show();
                                            addToWantFab.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                                        }
                                        else{
                                            wantVisit.add(object.id);
                                            Toast.makeText(v.getContext(), "Добавлено в интересное", Toast.LENGTH_SHORT).show();
                                            addToWantFab.setImageResource(R.drawable.ic_baseline_bookmark_24);
                                        }
                                        String TAG = "updateList";

                                        dbUser.update("wantVisit", wantVisit).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(v.getContext(), "Войдите в аккаунт", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        addToVisitFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                        if(currentUser!=null){
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference dbUser = db.collection("users").document(currentUser.getUid());

                            dbUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();

                                        List<String> visited = (List<String>) document.get("visited");

                                        if(visited.contains(object.id)){
                                            visited.remove(object.id);
                                            Toast.makeText(v.getContext(), "Удалено из посещённого", Toast.LENGTH_SHORT).show();
                                            addToVisitFab.setImageResource(R.drawable.ic_baseline_done_24);
                                        }
                                        else{
                                            visited.add(object.id);
                                            Toast.makeText(v.getContext(), "Добавлено в посещённое", Toast.LENGTH_SHORT).show();
                                            addToVisitFab.setImageResource(R.drawable.ic_baseline_done_on);
                                        }
                                        String TAG = "updateList";

                                        dbUser.update("visited", visited).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(v.getContext(), "Войдите в аккаунт", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        return v;
    }
}