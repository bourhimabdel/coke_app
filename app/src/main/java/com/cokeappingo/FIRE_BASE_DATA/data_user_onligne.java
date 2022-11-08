package com.cokeappingo.FIRE_BASE_DATA;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.util.StringUtil;

import com.cokeappingo.class_utile.account;
import com.cokeappingo.class_utile.comment;
import com.cokeappingo.class_utile.generateur;
import com.cokeappingo.class_utile.new_recipe;
import com.cokeappingo.class_utile.new_recipe_search;
import com.cokeappingo.sql_lite_manager.sql_manager;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class data_user_onligne {


    public void add_new_user(account client, final add_succeful add_succeful){
        //if(client.getImage()!=null) {
            FirebaseFirestore.getInstance().document("Users/ID user"+client.getAccount_ID())
                    .set(client).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    add_succeful.add_complet(true);
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    add_succeful.add_complet(false);
                }
            });
       // }else {
       //     FirebaseDatabase.getInstance().getReference()
       //             .child("Users").child("User ID" + client.getAccount_ID()).child("name").setValue(client.getName())
       //             .addOnSuccessListener(new OnSuccessListener<Void>() {
       //         @Override
       //         public void onSuccess(Void aVoid) {
       //             add_succeful.add_complet(true);
       //         }
       //     }).addOnFailureListener(new OnFailureListener() {
       //         @Override
       //         public void onFailure(@NonNull Exception e) {
       //             add_succeful.add_complet(false);
       //         }
       //     });
       //     FirebaseDatabase.getInstance().getReference()
       //             .child("Users").child("User ID" + client.getAccount_ID()).child("Bio").setValue(client.getBio())
       //             .addOnSuccessListener(new OnSuccessListener<Void>() {
       //                 @Override
       //                 public void onSuccess(Void aVoid) {
       //                     add_succeful.add_complet(true);
       //                 }
       //             }).addOnFailureListener(new OnFailureListener() {
       //         @Override
       //         public void onFailure(@NonNull Exception e) {
       //             add_succeful.add_complet(false);
       //         }
       //     });
       // }
    }

    public void if_user_exist(String number, final user_exist user_exist){
        FirebaseFirestore.getInstance().document("Users/ID user"+number).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            user_exist.exist(true,documentSnapshot.toObject(account.class));
                        }
                        else{
                            user_exist.exist(false,null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
       // FirebaseDatabase.getInstance().getReference().child("Users").child("User ID"+number).addListenerForSingleValueEvent(new ValueEventListener() {
       //     @Override
       //     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
       //         if (dataSnapshot.exists()){
       //             user_exist.exist(true,dataSnapshot.getValue(account.class));
       //         }
       //         else{
       //             user_exist.exist(false,null);
       //         }
       //     }
       //     @Override
       //     public void onCancelled(@NonNull DatabaseError databaseError) {
       //     }
       // });
    }

    public void add_picture(final Uri Picture, final add_succeful_puc add_succeful){
        final StorageReference storageReference=FirebaseStorage.getInstance().getReference();
        Totale_Number_recipe(new total_number_recipe_update() {
            @Override
            public void callback(int new_number) {
                if (new_number!=0) {
                    final int finalM = new_number;
                    storageReference.child("recipe_image").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(finalM+"").putFile(Picture).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            add_succeful.add_complet(null, finalM);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child("recipe_image")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(finalM+"").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    add_succeful.add_complet(uri.toString(), finalM);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void update_picture_recipe(String id_image,final Uri Picture, final add_succeful_puc add_succeful){
        final StorageReference storageReference=FirebaseStorage.getInstance().getReference();
        final String finalM = id_image;
        storageReference.child("recipe_image").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(finalM).putFile(Picture).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                add_succeful.add_complet(null, 0);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.child("recipe_image")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(finalM+"").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                add_succeful.add_complet(uri.toString(), 0);
                            }
                        });
            }
        });
    }

    public void update_profile_puc(final String id_user,final Uri Picture, final update_profile update_profile){
        final StorageReference storageReference=FirebaseStorage.getInstance().getReference();
        storageReference.child("User_profile_image").child(id_user).putFile(Picture).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.child("User_profile_image").child(id_user).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                       update_profile.oncallback(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                update_profile.oncallback(null);
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                update_profile.oncallback(null);
            }
        });
    }

    public void add_new_recipe(final new_recipe new_recipe, final return_id_push return_id_push){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        final String key=db.collection("RECIPE_USER").document().getId();
        new_recipe.setId_push(key);

        db.document("RECIPE_USER/"+key).set(new_recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                add_recipe_to_user(new_recipe.getAuteur_ID(), key, new add_succeful() {
                    @Override
                    public void add_complet(boolean etat) {
                        return_id_push.add_complet(key);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                return_id_push.add_complet(null);
            }
        });
    }

    public void add_recipe_to_user(String id,String key,final add_succeful add_succeful){
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        HashMap<String,String> map=new HashMap<>();
        map.put(key,key);

        db.document("Resipes one user/id "+id+"/all_id_push_recipe/"+key).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                add_succeful.add_complet(true);
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                add_succeful.add_complet(false);
            }
        });
    }

    public void retrive_Tocken(String Key, final return_id_push return_id_push) {
        FirebaseFirestore.getInstance().document("Tokens/"+Key).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Map<String, Object> myListOfDocuments2= documentSnapshot.getData();

                            for (Map.Entry mapentry : myListOfDocuments2.entrySet()) {
                                return_id_push.add_complet(mapentry.getValue().toString());
                            }
                        }
                        else{
                            return_id_push.add_complet(null);
                        }
                    }
                });
    }


    public void up_date_recipe(new_recipe up_date_recipe, final add_succeful add_succeful){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.document("RECIPE_USER/"+up_date_recipe.getId_push()).set(up_date_recipe)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                add_succeful.add_complet(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                add_succeful.add_complet(false);
            }
        });
    }

    public void retrive_recipe_user_by_KEY(String Key, final retrive_recipe retrive_recipe){
        FirebaseFirestore.getInstance().document("RECIPE_USER/"+Key).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            retrive_recipe.onCallback(documentSnapshot.toObject(new_recipe.class));
                        }
                        else{
                            retrive_recipe.onCallback(null);
                        }
            }
        });
    }

    public void Totale_Number_recipe(final total_number_recipe_update total_number_recipe_update){
        FirebaseFirestore.getInstance().document("Totale Number Recipe/Totale Number")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int tot=Integer.parseInt(String.valueOf(documentSnapshot.get("Number")));
                tot++;
                final int finalTot = tot;

                HashMap<String,Integer> map=new HashMap<>();

                map.put("Number",tot);

                FirebaseFirestore.getInstance().document("Totale Number Recipe/Totale Number").set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        total_number_recipe_update.callback(finalTot);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        total_number_recipe_update.callback(0);
                    }
                });
            }
        });
    }

    public void update_number_recipe_user(final account account,final total_number_recipe_update total_number_recipe_update){
        FirebaseFirestore.getInstance().document("Users/ID user"+account.getAccount_ID())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                account acc =documentSnapshot.toObject(account.class);
                int tot=acc.getNumber_recipe();
                tot++;
                final int finalTot = tot;


                account.setNumber_recipe(tot);
                account.setPhoto_saved(null);
                FirebaseFirestore.getInstance().document("Users/ID user"+account.getAccount_ID()).set(account).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        total_number_recipe_update.callback(finalTot);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        total_number_recipe_update.callback(0);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                total_number_recipe_update.callback(0);
            }
        });
    }

    public void get_all_key_user_recipe(String id, final key_user_recipe key_user_recipe){
        final ArrayList<String> keys=new ArrayList<>();

        FirebaseFirestore.getInstance().collection("Resipes one user/id "+id+"/all_id_push_recipe").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(queryDocumentSnapshots.getDocuments().size()!=0) {
                    for (DocumentSnapshot documentChange:queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> myListOfDocuments2 = documentChange.getData();
//
                        for (Map.Entry mapentry : myListOfDocuments2.entrySet()) {
                            keys.add(mapentry.getKey().toString());
                        }
//
                        key_user_recipe.on_callback(keys);
                    }
                }else
                    key_user_recipe.on_callback(keys);
            }
        });
    }


    public void increment_number_recipe(int new_number,account account, final add_succeful add_succeful){
        account.setNumber_recipe(new_number);
        account.setPhoto_saved(null);
        FirebaseFirestore.getInstance().document("Users/ID user"+account.getAccount_ID()).set(account).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                add_succeful.add_complet(true);
            }
        });
    }

    public void add_recipe_to_List_attent_admin(String key,final add_succeful add_succeful){

        HashMap<String,String> map=new HashMap<>();

        map.put(key,key);

        FirebaseFirestore.getInstance().document("Resipes_attent/"+key)
                .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                add_succeful.add_complet(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                add_succeful.add_complet(false);
            }
        });
    }

    public void delet_recipe_into_puck(new_recipe new_recipe,final add_succeful add_succeful){

        FirebaseDatabase.getInstance().getReference().child("RECIPE_USER_SEARCH").child(new_recipe.getId_push())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        new_recipe_search new_recipe_search=snapshot.getValue(new_recipe_search.class);

                        Log.e("njnj",new_recipe_search.getPuck().toString());

                        FirebaseDatabase.getInstance().getReference().child("recipe_visible")
                                .child("all_categories").child("all_cuisine").child(new_recipe_search.getPuck().get(2)+"")
                                .child(new_recipe.getId_push()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReference().child("recipe_visible")
                                        .child(new_recipe.getCategories()).child("all_cuisine").child(new_recipe_search.getPuck().get(1)+"")
                                        .child(new_recipe.getId_push()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference().child("recipe_visible")
                                                .child(new_recipe.getCategories()).child(new_recipe.getCuisine()).child(new_recipe_search.getPuck().get(0)+"")
                                                .child(new_recipe.getId_push()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                add_succeful.add_complet(true);
                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    //  FirebaseFirestore.getInstance().document("RECIPE_USER_SEARCH/"+new_recipe.getId_push()).get()
    //          .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
    //              @Override
    //              public void onSuccess(DocumentSnapshot documentSnapshot) {
    //                  if (documentSnapshot.exists()){
    //                      new_recipe_search new_recipe_search=documentSnapshot.toObject(new_recipe_search.class);

    //                      FirebaseFirestore.getInstance().document("recipe_visible/all_categories/all_cuisine/"+new_recipe_search.getPuck()
    //                              .get(0)+"/recipe/"+new_recipe.getId_push()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
    //                          @Override
    //                          public void onSuccess(Void aVoid) {

    //                              FirebaseFirestore.getInstance().document("recipe_visible/"+recipe_visible+"/all_cuisine/"+new_recipe_search.getPuck()
    //                                      .get(1)+"/recipe/"+new_recipe.getId_push()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
    //                                  @Override
    //                                  public void onSuccess(Void aVoid) {

    //                                      FirebaseFirestore.getInstance().document("recipe_visible/"+new_recipe.getCategories()+"/"+new_recipe.getCuisine()+"/"+new_recipe_search.getPuck()
    //                                              .get(2)+"/recipe/"+new_recipe.getId_push()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
    //                                          @Override
    //                                          public void onSuccess(Void aVoid) {

    //                                              add_succeful.add_complet(true);

    //                                          }
    //                                      });

    //                                  }
    //                              });

    //                          }
    //                      });
    //                  }
    //                  else{

    //                  }
    //              }
    //          });
    }


    public void delet_recipe_from_firebase(new_recipe new_recipe,String id,String lien,String status,String id_push,final add_succeful add_succeful) {
        if (status.equals("ok")) {

            delet_recipe_into_puck(new_recipe, new add_succeful() {
                @Override
               public void add_complet(boolean etat) {

                    FirebaseDatabase.getInstance().getReference().child("RECIPE_USER_SEARCH")
                            .child(id_push).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseFirestore.getInstance().document("RECIPE_USER/" + id_push)
                                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseFirestore.getInstance().document("Resipes_attent/" + id_push).delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    FirebaseFirestore.getInstance().document("Resipes one user/id " + id + "/all_id_push_recipe/" + id_push)
                                                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(lien);
                                                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // File deleted successfully
                                                                    add_succeful.add_complet(true);
                                                                    Log.e("firebasestorage", "onSuccess: deleted file");
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception exception) {
                                                                    // Uh-oh, an error occurred!
                                                                    add_succeful.add_complet(true);
                                                                    Log.e("firebasestorage", "onFailure: did not delete file");
                                                                }
                                                            });

                                                        }
                                                    });
                                                }
                                            });
                                }
                            });
                        }
                    });


                }
            });

        } else {

            FirebaseFirestore.getInstance().document("RECIPE_USER/" + id_push)
                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseFirestore.getInstance().document("Resipes_attent/" + id_push).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseFirestore.getInstance().document("Resipes one user/id " + id + "/all_id_push_recipe/" + id_push)
                                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(lien);
                                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // File deleted successfully
                                                    add_succeful.add_complet(true);
                                                    Log.e("firebasestorage", "onSuccess: deleted file");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Uh-oh, an error occurred!
                                                    add_succeful.add_complet(true);
                                                    Log.e("firebasestorage", "onFailure: did not delete file");
                                                }
                                            });

                                        }
                                    });
                                }
                            });
                }
            });

        }
    }

    public void get_keys_of_recipes_in_this_pack(String num_pack,final key_user_recipe key_user_recipe){
        ArrayList<String> keys=new ArrayList<>();
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("recipe_visible").child("all_categories")
                .child("all_cuisine").child(num_pack);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    keys.add(snap.getValue(String.class));
                }
                key_user_recipe.on_callback(keys);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error","we can not get recipe of this pack :"+num_pack);
            }
        });
    }

    public void get_number_current_pack_of_a_specific_cuisine(String exact_categories,String exact_Cuisine,final number_current_pack number_current_pack){
        FirebaseDatabase.getInstance().getReference().child("recipe_visible").child(exact_categories).child(exact_Cuisine)
                .child("generator").child("pack_number")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        number_current_pack.onCallback(String.valueOf(snapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void get_keys_of_recipes_in_this_pack_into_cuisine(String exact_categories,String exact_Cuisine,String num_pack,final get_recipe_exact_search GG){
        ArrayList<new_recipe_search> keys=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("recipe_visible")
                .child(exact_categories).child(exact_Cuisine)
                .child(num_pack+"").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    for (DataSnapshot d:snapshot.getChildren())
                        keys.add(d.getValue(new_recipe_search.class));

                GG.on_callback(keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void get_all_Title(Context context,get_titles get_titles){
       // sql_manager sql_manager=new sql_manager(context);
        HashMap<String,String> all_title=new HashMap<>();
       // if(!sql_manager.get_TABLE_ALL_TITLE().containsKey("-MSNqedubGwWkmrPtyGs")){
            FirebaseDatabase.getInstance().getReference().child("All Title Of Recipe").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                      //  sql_manager.insert_data_into_TABLE_ALL_TITLE(snap.getKey(),snap.getValue(String.class));
                        all_title.put(snap.getKey(),snap.getValue(String.class));
                    }
                    get_titles.on_callback(all_title);
                    //add_succeful.add_complet(true);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error){
                }
            });
      //  }else
      //     // add_succeful.add_complet(true);
    }

    public void add_a_comment_to_recipe(String id_push,comment comment,add_succeful add_succeful){

        FirebaseDatabase.getInstance().getReference().child("Comment_for_recipes")
                .child("recipe_id_push"+ id_push).child(comment.getId_visitor()).setValue(comment)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                add_succeful.add_complet(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                add_succeful.add_complet(false);
            }
        });

    }

    public void get_all_comment_recipe(String id, final get_comment get_comment){
        final ArrayList<comment> keys=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Comment_for_recipes")
                .child("recipe_id_push"+ id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()) {
                   for (DataSnapshot s : snapshot.getChildren())
                       keys.add(s.getValue(comment.class));
                   get_comment.on_callback(keys);
               }
                else
                   get_comment.on_callback(keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void delete_comment(String id_push,comment comment,add_succeful add_succeful){
        FirebaseDatabase.getInstance().getReference().child("Comment_for_recipes")
                .child("recipe_id_push"+ id_push).child(comment.getId_visitor()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        add_succeful.add_complet(true);
                    }
                });
    }

    public void Search_for_this_text(String search_text,get_recipe_exact_search get_recipe_exact_search){

        ArrayList<new_recipe_search> DataCache=new ArrayList<>();
        DatabaseReference db=FirebaseDatabase.getInstance().getReference();
        Query firebaseSearchQuery = db.child("RECIPE_USER_SEARCH").orderByChild("title").startAt(search_text)
                .endAt(search_text + "uf8ff");
        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataCache.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Now get Scientist Objects and populate our arraylist.
                        new_recipe_search scientist = ds.getValue(new_recipe_search.class);
                        DataCache.add(scientist);
                        Log.e("t",scientist.getTitle());
                    }
                }
                get_recipe_exact_search.on_callback(DataCache);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE CRUD", databaseError.getMessage());
            }
        });
    }



    public interface add_succeful{
        void add_complet(boolean etat);
    }

    public interface return_id_push{
        void add_complet(String id_push);
    }

    public interface add_succeful_puc{
        void add_complet(String Url,int id);
    }

    public interface user_exist{
        void exist(boolean etat,account r);
    }

    public interface total_number_recipe_update{
        void callback(int new_number);
    }

    public interface update_profile{
       void oncallback(String Url);
    }

    public interface key_user_recipe{
        void on_callback(ArrayList<String> keys);
    }

    public interface get_comment{
        void on_callback(ArrayList<comment> comments);
    }

    public interface retrive_recipe{
        void onCallback(new_recipe recipe);
    }

    public interface number_current_pack{
        void onCallback(String number);
    }

    public interface get_titles{
        void on_callback(HashMap<String,String> title);
    }

    public interface get_recipe_exact_search{
        void on_callback(ArrayList<new_recipe_search> recipes);
    }
}
