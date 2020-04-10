package ai.sper.helloworld001.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigProject {
    private String projectName;
    private Right right;

    public enum  Right {
        // TODO: 4/10/20 refactor these
        READ, WRITE, NONE;
    }
}
