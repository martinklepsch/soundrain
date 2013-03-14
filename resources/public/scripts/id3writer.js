


var ID3Writer = {
  // input is an array of objects following this pattern:
  //    {frameType: <frame-type>, data: <data>}
  create: function(input, data) {
    var finishedFrames = new Array();

    for (var i=0; i<input.length; i++) {
      finishedFrames.push(this.buildFrame(input[i].frameType, input[i].data));
    }

    var totalSize = finishedFrames.size;
    var header = this.calculateHeader(totalSize);
    return new Blob(header + finishedFrames + [data]);
  },

  buildFrame: function(frameType, data) {
    var dataBlob = new Blob([data]);
    var length = dataBlob.size;

    var sizeBuffer = new ArrayBuffer(4);
    var int32View = new Int32Array(sizeBuffer);
    int32View[0] = length;

    var frameTypeBuffer = new ArrayBuffer(4);
    var Uint8View = new Uint8Array(frameTypeBuffer);
    for (int i=0; i<4; i++) {
      Uint8View[i] = frameType.charCodeAt(i);
    }
 
    //               frameType, size,      flags,  UTF-16, data 
    return new Blob([Uint8View, int32View, "\0\0", "\x01", dataBlob]));
  },

  syncSafeSize: function(size) {
    var buffer = new ArrayBuffer(4);
    var ret = new Uint8Array(buffer);
    for (int i=3; i>=0; i--) {
      ret[3-i] = (size & (0x7F << i*7)) >> (i*7);
    }
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