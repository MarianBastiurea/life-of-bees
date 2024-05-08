package com.marianbastiurea.lifeofbees;
import java.util.Date;

public class EggsBatch {
  private int numberOfEggs;
   private Date creationDate;

   public EggsBatch(int numberOfEggs, Date creationDate) {
       this.numberOfEggs = numberOfEggs;
       this.creationDate = creationDate;
   }

    public int getNumberOfEggs() {
        return numberOfEggs;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setNumberOfEggs(int numberOfEggs) {
        this.numberOfEggs = numberOfEggs;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "EggsBatch{" +
                "numberOfEggs=" + numberOfEggs +
                ", creationDate=" + creationDate +
                '}';
    }
}
