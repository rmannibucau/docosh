# build:
# docker build --tag docosh:latest .
#
# run examples:
# docker run docosh:latest
# docker run docosh:latest /bin/docosh-1.0-SNAPSHOT help list-images
# docker run docosh:latest /bin/docosh-1.0-SNAPSHOT list-images ....

FROM rmannibucau/jlink-base:11

COPY target/docosh-1.0-SNAPSHOT /bin/

CMD [ "/bin/docosh-1.0-SNAPSHOT" ]

