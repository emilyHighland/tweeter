package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;
import java.util.Objects;

public class StoryResponse extends PagedResponse{

    private List<Status> statuses;

    private StoryResponse(){}

    public StoryResponse(String message){
        super(false, message, false);
    }

    public StoryResponse(boolean success, boolean hasMorePages){
        super(success,hasMorePages);
    }

    public StoryResponse(List<Status> statuses, boolean hasMorePages){
        super(true, hasMorePages);
        this.statuses = statuses;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StoryResponse that = (StoryResponse) o;

        return (Objects.equals(statuses, that.statuses) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(statuses);
    }
}
