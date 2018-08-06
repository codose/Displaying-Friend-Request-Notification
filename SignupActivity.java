/*
This Activity is named as RegisterActivity in the given tutorial series. 
here is the method that should be called in case the REGISTER_ACCOUNT button is pressed.
--firebaseDatabaseReference is the reference to the database
the code being presented here is working fine on the day August 06,2018.
*/

    void registerAccount(View view){

       if(!TextUtils.isEmpty(email_txt.getText()) || !TextUtils.isEmpty(password_txt.getText())|| !TextUtils.isEmpty(name_txt.getText()))
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        String email=email_txt.getText ().toString ().trim ();
        String password=password_txt.getText ().toString ();

        firebaseAuth.createUserWithEmailAndPassword (  email,password).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
              current_user=FirebaseAuth.getInstance().getCurrentUser();
               uid=current_user.getUid();

              firebaseDatabaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
//getting device_id 
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( SignupActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            deviceId = instanceIdResult.getToken();

                            HashMap<String,String> userMap=new HashMap<>();
                            userMap.put("name",name_txt.getText().toString());
                            userMap.put("status","Hi there!");
                            userMap.put("image","default");
                            userMap.put("thumb_img","default");
                           //updating device-id in database
			    userMap.put("device_token",deviceId);

                           
                            firebaseDatabaseReference.setValue (userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful ()){
                                        Toast.makeText ( getApplicationContext (),"Registered Successfully",Toast.LENGTH_SHORT ).show ();
                                        Intent intent=new Intent(SignupActivity.this,GettingUserDetails.class);
                                        intent.putExtra("user_id",uid);
                                        startActivity(intent);
                                        finish ();
                                        progressDialog.dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText ( getApplicationContext (),"Registration Error",Toast.LENGTH_SHORT ).show ();
                                        progressDialog.dismiss();}         }
                            });



                        }
                    });


          } else
                {Toast.makeText ( getApplicationContext (),"Couldn't create Account!",Toast.LENGTH_SHORT ).show ();
                    progressDialog.dismiss();}

            }
                   } );

    }
