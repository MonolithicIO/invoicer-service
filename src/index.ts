import app from './application/app';
import "reflect-metadata";

const port = 3000;

app.listen(port, () => {
  console.log(`server running on port ${port}`);
});
