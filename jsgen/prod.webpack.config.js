var webpack = require('webpack');
const resolve = require('path').resolve;

module.exports.plugins = (module.exports.plugins || []).concat([
  new webpack.optimize.UglifyJsPlugin(),
  new webpack.optimize.DedupePlugin()
]);

