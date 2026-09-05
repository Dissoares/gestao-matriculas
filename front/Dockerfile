FROM node:20-alpine AS build
RUN apk add --no-cache libc6-compat
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN NX_DAEMON=false npx nx build webApp --configuration=production --skip-nx-cache

FROM nginx:alpine
COPY --from=build /app/dist/apps/webApp/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
