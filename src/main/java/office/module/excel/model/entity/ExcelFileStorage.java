package office.module.excel.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Blob;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@ToString
public class ExcelFileStorage {
    @Id @NotNull
    private String id;
    private Blob fileData;
    private String fileName;
    private String url;
    private String contentType;
    private String creator;
    private String owner;
    @CreationTimestamp
    private Date createDate;
}
