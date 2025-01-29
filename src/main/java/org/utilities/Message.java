package org.utilities;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String from;
    private String content;
    private String files[];
    private String subjects;
}
