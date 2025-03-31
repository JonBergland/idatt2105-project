FROM node:18-alpine as build-stage

# Set working directory
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm ci

# Copy project files
COPY . .

# Build the app
RUN npm run build-only

# Production stage
FROM nginx:stable-alpine as production-stage

# Copy built assets from the build stage
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Expose port
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]