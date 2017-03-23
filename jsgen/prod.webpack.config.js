var webpack = require('webpack');
const resolve = require('path').resolve;

require('target/scala-2.12/scalajs-bundler/main/node_modules/bootstrap/dist/css/bootstrap.css');
require('target/scala-2.12/scalajs-bundler/main/node_modules/bootstrap/dist/css/bootstrap-theme.css');

module.exports = require('./scalajs.webpack.config');
module.exports.module = {
    rules: [
        {
            test: /\.css$/,
            include: resolve('./target/scala-2.12/scalajs-bundler/main/node_modules/bootstrap/'),
            use: [ 'style-loader', 'css-loader' ]
        }
    ]
};

module.exports.plugins = (module.exports.plugins || []).concat([
  new webpack.optimize.UglifyJsPlugin(),
  new webpack.optimize.DedupePlugin()
]);

