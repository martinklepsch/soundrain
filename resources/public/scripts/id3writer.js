


var ID3Writer = {
  // input is an array of objects following this pattern:
  //    {frameType: <frame-type>, data: <data>, <optional> isBinary: <bool>}
  create: function(input, data) {
    var finishedFrames = new Array();

    for (var i=0; i<input.length; i++) {
      finishedFrames.push(this.buildFrame(input[i].frameType, input[i].data, input[i].isBinary || false));
      
    }

    var finishedFramesBlob = new Blob(finishedFrames);
    var totalSize = finishedFramesBlob.size;
    var header = this.calculateHeader(totalSize);
    var array = [header];
    array.push(finishedFramesBlob);
    array.push(data);
    return new Blob(array, {type: 'audio/mp3'});
  },

  buildFrame: function(frameType, data, isBinary) {
    var dataBlob = new Blob([data]);
    var length = dataBlob.size+1;

    var sizeBuffer = new ArrayBuffer(4);
    var dataView = new DataView(sizeBuffer);
    dataView.setInt32(0, length, false);

    var frameTypeBuffer = new ArrayBuffer(4);
    var Uint8View = new Uint8Array(frameTypeBuffer);
    for (var i=0; i<4; i++) {
      Uint8View[i] = frameType.charCodeAt(i);
    }
 
    if (isBinary) {
      //               frameType, size,     flags,  data 
      return new Blob([Uint8View, dataView, "\0\0", dataBlob]);
    } else {
      //               frameType, size,     flags,  UTF-8,  data 
      return new Blob([Uint8View, dataView, "\0\0", "\x03", dataBlob]);
    }
  },

  syncSafeSize: function(size) {
    var buffer = new ArrayBuffer(4);
    var ret = new Uint8Array(buffer);
    for (var i=3; i>=0; i--) {
      ret[3-i] = (size & (0x7F << i*7)) >> (i*7);
    }
    console.log(size);
    console.log(ret);
    return ret;
  },

  calculateHeader: function(size) {
    return new Blob(["\x49\x44\x33", // "ID3", yes Strings are cumbersome in JS
                    "\x03\x00", // version
                    "\0", // flags
                    this.syncSafeSize(size)]) //size
  }

  // Utils for converting between UTF-16 strings and arraybuffers
  // ab2str: function(buf) {
  //   return String.fromCharCode.apply(null, new Uint16Array(buf));
  // },
  // str2ab: function(str) {
  //   var buf = new ArrayBuffer(str.length*2); // 2 bytes for each char
  //   var bufView = new Uint16Array(buf);
  //   for (var i=0, strLen=str.length; i<strLen; i++) {
  //     bufView[i] = str.charCodeAt(i);
  //   }
  //   return buf;
  // },

  // UTF-16 byte order mark
  // bom: "\ufeff",
}