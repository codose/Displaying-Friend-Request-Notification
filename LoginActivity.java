/*
This method should be implemented while LOGIN_BUTTON is pressed .
it will login to the account and will update the device_id as well while signing-in to the account
the code being presented here is working fine on the day August 06,2018.  

*/
public void continueLogin(View view){
progressDialog.setMessage("Logging in...");
progressDialog.show();
    firebaseAuth.signInWithEmailAndPassword(user_email.getText().toString().trim(),user_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
/                
//here the device_id is being taken
FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        deviceId = instanceIdResult.getToken();

                        String current_user_id=firebaseAuth.getCurrentUser().getUid();
//here it is being updated to the database
                        databaseReference.child(current_user_id).child("device_token").setValue(deviceId).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(getApplicationContext(),"Logged-in Successfully",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"error here",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });


            }
            else
            {
                Toast.makeText(getApplicationContext(),"Logged-in Error",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    })    ;
    }

