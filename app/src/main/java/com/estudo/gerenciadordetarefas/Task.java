package com.estudo.gerenciadordetarefas;
//01
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "taskId")
    private int mId;

    @Nullable
    @ColumnInfo(name = "descriptionTask")
    private String mDescription;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(@Nullable String description) {
        mDescription = description;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public float getPriorityLevel() {
        return mPriorityLevel;
    }

    public void setPriorityLevel(float priorityLevel) {
        mPriorityLevel = priorityLevel;
    }

    public long getEstimatedDate() {
        return mEstimatedDate;
    }

    public void setEstimatedDate(long estimatedDate) {
        mEstimatedDate = estimatedDate;
    }

    public long getInsertionDate() {
        return mInsertionDate;
    }

    public void setInsertionDate(long insertionDate) {
        mInsertionDate = insertionDate;
    }

    public long getUpdateDate() {
        return mUpdateDate;
    }

    public void setUpdateDate(long updateDate) {
        mUpdateDate = updateDate;
    }

    public int getIsFinished() {
        return mIsFinished;
    }

    public void setIsFinished(int isFinished) {
        mIsFinished = isFinished;
    }

    public int getIsDeleted() {
        return mIsDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        mIsDeleted = isDeleted;
    }

    public Task(@Nullable String description, String note, float priorityLevel, long estimatedDate,
                long insertionDate, long updateDate, int isFinished, int isDeleted) {
        mDescription = description;
        mNote = note;
        mPriorityLevel = priorityLevel;
        mEstimatedDate = estimatedDate;
        mInsertionDate = insertionDate;
        mUpdateDate = updateDate;
        mIsFinished = isFinished;
        mIsDeleted = isDeleted;
    }

    private String mNote;
    private float mPriorityLevel;
    private long mEstimatedDate;
    private long mInsertionDate;
    private long mUpdateDate;
    private int mIsFinished;
    private int mIsDeleted;

}
