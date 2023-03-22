package pm.workout.helper.domain.user.health;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.user.AppUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users_photos")
public class AppUserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGeneratorUsersHealthDetails")
    @SequenceGenerator(name = "sequenceGeneratorUser", sequenceName = "users_health_details_sequence", allocationSize = 1)
    private Long id;

    private String name;

    private LocalDateTime uploadedTime;

    private String type;

    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;

    @ManyToOne(fetch = FetchType.EAGER)
    private AppUser appUser;

    public void linkWithUser(AppUser appUser){
        this.appUser = appUser;
    }

}

