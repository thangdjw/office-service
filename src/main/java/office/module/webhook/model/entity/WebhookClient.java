package office.module.webhook.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter @Getter
@Entity
@Table(name = "webhook_client")
@DynamicUpdate
public class WebhookClient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "request_method", length = 20, nullable = false)
    private String requestMethod;

    @NotNull
    @Column(name = "request_address", nullable = false)
    private String requestAddress;

    @Size(max = 65535)
    @Column(name = "request_header", length = 65535)
    private String requestHeader;

    @Size(max = 65535)
    @Column(name = "request_body", length = 65535)
    private String requestBody;

    @NotNull
    @Column(name = "times", nullable = false)
    private Integer times;

    @NotNull
    @Column(name = "topic", nullable = false)
    private String topic;

    @NotNull
    @Column(name = "processing", nullable = false)
    private Boolean processing;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_send", nullable = false)
    private Date lastSend;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "next_send", nullable = false)
    private Date nextSend;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false)
    private Date createdOn;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_on", nullable = false)
    private Date modifiedOn;
}
