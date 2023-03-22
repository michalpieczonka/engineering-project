FROM node:16.17.1
WORKDIR /usr/src/react-app

COPY package*.json ./

RUN npm install --force

# Copy the rest of the app's files to the container
COPY . .
EXPOSE 3000
# Specify the command to run the app
CMD [ "npm", "start" ]