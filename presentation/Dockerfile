FROM timbru31/node-chrome:latest
WORKDIR /home/marp
RUN npm install --save-dev @marp-team/marp-cli
RUN npm install --save-dev markdown-it-plantuml
COPY slides.md /home/marp
COPY marp.config.js /home/marp
RUN npx marp --config-file marp.config.js --html slides.md
CMD ["/bin/bash"]