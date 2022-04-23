/** @format */

import bodyParser from 'body-parser';
import express from 'express';
import {Server as HttpServer} from 'http';
import * as SocketIO from 'socket.io';
import {fileMiddleWare} from './middleware/file';
import {handleImageRequests, proxyRequest} from './service';
import handleIo from './socket';
import corsConfig from './socket/cors.config.json';
import cookieParser from 'cookie-parser';
import { corsMiddleware } from './middleware/corsMiddleware';

const port = process.env.PORT ? parseInt(process.env.PORT) : 8078;
const app = express();
app.use(corsMiddleware);
app.use(cookieParser());
app.use(fileMiddleWare);
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.all('/api/v1/images', handleImageRequests);
app.all('/api/v1/images/:path', handleImageRequests);

app.all('/api/v1/profiles',(req, res) => proxyRequest(req, res, 'PROFILE-SERVICE'));
app.all('/api/v1/profiles/:path',(req, res) => proxyRequest(req, res, 'PROFILE-SERVICE'));

app.all('/api/v1/rooms',(req, res) => proxyRequest(req, res, 'ROOM-SERVICE'));
app.all('/api/v1/rooms/:path',(req, res) => proxyRequest(req, res, 'ROOM-SERVICE'));

app.all('/api/v1/messages',(req, res) => proxyRequest(req, res, 'MESSAGE-SERVICE'));
app.all('/api/v1/messages/:path',(req, res) => proxyRequest(req, res, 'MESSAGE-SERVICE'));

app.all('/api/v1/authentication', (req, res) => proxyRequest(req, res, 'AUTHENTICATION-SERVICE'));
app.all('/api/v1/authentication/:path', (req, res) => proxyRequest(req, res, 'AUTHENTICATION-SERVICE'));

const httpServer = new HttpServer(app);
const socketIoServer = new SocketIO.Server(httpServer, corsConfig);
handleIo(socketIoServer);
httpServer.listen(port, () => console.log(`server is running on port ${port}`));
