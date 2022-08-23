package com.estudo.gerenciadordetarefas;
//03

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class DbRoomDatabase extends RoomDatabase {

  public abstract  TaskDao mTaskDao();
        private  static  RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db){
                super.onOpen(db);
                //new PopulateDbAsync(INSTANCE).execute();
            }
        };

        private static DbRoomDatabase INSTANCE;

        public static DbRoomDatabase getDatabase(final Context context){
            if (INSTANCE == null){

                synchronized (DbRoomDatabase.class){
                    if ( INSTANCE == null ){

                        INSTANCE = Room.databaseBuilder(
                              context.getApplicationContext(),
                              DbRoomDatabase.class,"db_task_list_app"
                        ).fallbackToDestructiveMigration()
                         .addCallback(sRoomDatabaseCallback)
                         .build();
                    }
                }

            }

            return INSTANCE;
        }

        private static class PopulateDbAsync extends AsyncTask<Void,Void,Void>{
            private final TaskDao mTaskDao;
            PopulateDbAsync(DbRoomDatabase mDbRoomDatabase){
                mTaskDao = mDbRoomDatabase.mTaskDao();
            }
            @Override
            protected Void doInBackground(final Void...params){
                mTaskDao.deleteAllTasks();
                Task task = new Task(

                        "Teste com descrição",
                        "Teste com Observação",
                        0,0,0,0,0,0
                );
                mTaskDao.insertTask(task);
                return null;
            }

        }

}
