"use strict";

/* printAlbum */
let elements = document.getElementsByClassName("album-item");
let output = "";
for (let index = 0; index < elements.length; index++) {
    let ele = elements[index];
    let album = ele.children[1].children[0].innerText;
    let author = ele.children[1].children[1].innerText;
    let grade = ele.children[1].children[2].children[0].children[1].innerText;
    let date = ele.children[1].children[3].innerText;
    let link =
        ele.children[0].children[0].children[0].children[0].children[0].src;
    link = link.substring(0, link.indexOf("?"));
    output +=
        author + ",," + album + ",," + date + ",," + grade + ",," + link + "\n";
}
console.log(output);
