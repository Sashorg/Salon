package com.example.salon.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.salon.Adapter.TypeAdapter;
import com.example.salon.Common.SpaceItemDecoration;
import com.example.salon.Interface.IAllSalonListener;
import com.example.salon.Interface.ITypeLoadListener;
import com.example.salon.Models.Salon;
import com.example.salon.R;
import com.facebook.all.All;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

import static com.facebook.AccessTokenManager.TAG;

public class BookingFragment1 extends Fragment implements IAllSalonListener, ITypeLoadListener {
    CollectionReference AllSalonReference; //database collection reference
    CollectionReference TypeReference;
    ITypeLoadListener iTypeLoadListener;
    IAllSalonListener iAllSalonListener;
    @BindView(R.id.spinner)
    MaterialSpinner materialSpinner;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    //read about it
    Unbinder unbinder;

    AlertDialog dialog ;
    static BookingFragment1 instance;
     public static BookingFragment1 getInstance() {

        if(instance==null){
            instance=new BookingFragment1();
        }
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AllSalonReference= FirebaseFirestore.getInstance().collection("AllSalon");
        iAllSalonListener=this;
        iTypeLoadListener=this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();
     }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.bookingfragment1,container,false);
        unbinder= ButterKnife.bind(this, itemView);
        initView();
        loadAllsalon();
return itemView;

     }

    private void initView() {
         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));// spancount is number of coulmns in the result
         recyclerView.addItemDecoration(new SpaceItemDecoration(4));
    }

    private void loadAllsalon() {
         AllSalonReference.get()
                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){

                        List<String> list = new ArrayList<>();
                        list.add("please choose genger");
                        for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                            Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            list.add(documentSnapshot.getId());}
                        iAllSalonListener.onAllSalonLoadSuccess(list);

                    }
                     }
                 }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 iAllSalonListener.onAllSalonLoadFailed(e.getMessage());
             }
         });
    }

    @Override
    public void onAllSalonLoadSuccess(List<String> areaNameList) {
    materialSpinner.setItems(areaNameList);
    materialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
            if(position>0){
                loadType(item.toString());
            }
        }
    });
    }

    private void loadType(String type) {
    dialog.show();
    TypeReference =FirebaseFirestore.getInstance().collection("AllSalon").document(type).collection("Type");
    TypeReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            List<Salon> list = new ArrayList<>();
            if(task.isSuccessful()){

                for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                    list.add(documentSnapshot.toObject(Salon.class));
                iTypeLoadListener.onTypeLoadSuccess(list);
            }
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            iTypeLoadListener.onTypeLoadFailed(e.getMessage());
        }
    });

    }

    @Override
    public void onAllSalonLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTypeLoadSuccess(List<Salon> salonListList) {
        TypeAdapter adapter =new TypeAdapter(getActivity(),salonListList);
        recyclerView.setAdapter(adapter);
        dialog.dismiss();


    }

    @Override
    public void onTypeLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }
}
