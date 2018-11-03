package com.ensicaen.pierre.fluxrss.data.db.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Item {

    private String title;
    private String pubDate;
    private String description;
    private String url;
    private byte[] image;

}