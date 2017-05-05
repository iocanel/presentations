FROM scratch

COPY bin/revealjs /usr/local/bin/revealjs


COPY slides/package.json /usr/local/share/slides/package.json

COPY slides/lib /usr/local/share/slides/lib
COPY slides/plugin /usr/local/share/slides/plugin
COPY slides/css /usr/local/share/slides/css
COPY slides/js /usr/local/share/slides/js
COPY slides/fonts /usr/local/share/slides/fonts
COPY slides/diagrams /usr/local/share/slides/diagrams
COPY slides/images /usr/local/share/slides/images
COPY slides/asciicasts /usr/local/share/slides/asciicasts
COPY slides/footer.reveal.js /usr/local/share/slides/footer.reveal.js

COPY slides/index.html /usr/local/share/slides/index.html

CMD ["/usr/local/bin/revealjs", "/usr/local/share/slides"]
