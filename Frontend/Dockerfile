FROM nginx:1.27-alpine

COPY index.html login.html main.html register.html style.css app*.js /usr/share/nginx/html/

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 1010

CMD ["nginx", "-g", "daemon off;"]