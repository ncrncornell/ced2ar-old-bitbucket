var webpack = require('webpack');
const resolve = require('path').resolve;

module.exports = require('./scalajs.webpack.config');
module.exports.plugins = (module.exports.plugins || []).concat([
  new webpack.optimize.UglifyJsPlugin(),
  new webpack.optimize.DedupePlugin()
]);

