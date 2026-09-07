FROM node:20-slim AS build
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps && \
    npm install @nx/nx-linux-x64-gnu@23.2.0 --no-save --legacy-peer-deps
COPY . .
ENV NX_DAEMON=false
ENV NX_SKIP_NX_CACHE=true
RUN npx nx build gestao-matrizes --configuration=production

FROM nginx:alpine
COPY --from=build /app/dist/apps/gestao-matrizes/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
