/*
Copyright (c) 2013, Maximilian Bachl
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL MAXIMILIAN BACHL BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

// Implicitly assumes UTF-8 as the encoding of your website
var ID3Writer = {

  // input is an array of objects following this pattern:
  //    {frameType: <frame-type>, data: <mp3Data>, <optional> isBinary: <bool>, <optional> coverMime: <mimeType>}
  // 
  // You can find a list of all frameTypes at 
  // http://en.wikipedia.org/wiki/ID3#ID3v2_Frame_Specification_.28Version_2.3.29
  create: function(input, data) {
    var finishedFrames = new Array();

    for (var i=0; i<input.length; i++) {
      if (input[i].frameType === 'APIC') {
        finishedFrames.push(this.buildCoverFrame(input[i].frameType, 
                            input[i].data, 
                            input[i].coverMime || "image/jpeg"));
      } else {
        finishedFrames.push(this.buildFrame(input[i].frameType, 
                            input[i].data, 
                            input[i].isBinary || false));
      }
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
    var length = dataBlob.size;
    if (!isBinary)
      length += 1;

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

  buildCoverFrame: function(frameType, data, coverMime) {
    var dataBlob = new Blob([data]);
    var coverMimeBlob = new Blob([coverMime]);
    var length = dataBlob.size + 4 + coverMimeBlob.size;

    var sizeBuffer = new ArrayBuffer(4);
    var dataView = new DataView(sizeBuffer);
    dataView.setInt32(0, length, false);

    var frameTypeBuffer = new ArrayBuffer(4);
    var Uint8View = new Uint8Array(frameTypeBuffer);
    for (var i=0; i<4; i++) {
      Uint8View[i] = frameType.charCodeAt(i);
    }
 
    //               frameType, size,     flags,  UTF-8,  mimeType,      used for cover, data
    return new Blob([Uint8View, dataView, "\0\0", "\x03", coverMimeBlob, "\0\x03\0", dataBlob]);
  },

  syncSafeSize: function(size) {
    var buffer = new ArrayBuffer(4);
    var ret = new Uint8Array(buffer);
    for (var i=3; i>=0; i--) {
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

}
